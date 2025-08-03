package uadb.location.contrat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.contrat.dto.client.ChambreClient.ReadChambreDTO;
import uadb.location.contrat.dto.client.UtilisateurClient.ReadUtilisateurDTO;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.contratController.InfoContratWChambreAndLocataireResponse;
import uadb.location.contrat.dto.controller.contratController.createContrat.CreateContratRequest;
import uadb.location.contrat.dto.controller.contratController.updateContrat.UpdateContratRequest;
import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.contrat.ContratSpecification;
import uadb.location.contrat.model.paiement.Paiement;
import uadb.location.contrat.repositories.ContratRepository;
import uadb.location.contrat.service.Interface.IContratService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ContratService implements IContratService {

    private final ContratRepository contratRepository;

    @Value("${gateway.service.url}")
    private String gatewayServiceUrl;

    @Value("${logement.service.url}")
    private String logementServiceUrl;

    public ContratService(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }


    @Override
    public Page<InfoContratResponse> getAllContrat(Pageable pageable, ContratSearchCriteria contratSearchCriteria, Long userId) {
        if(contratSearchCriteria.locataireId() == null){
            RestClient restClient = RestClient.create(logementServiceUrl);
            ReadChambreDTO[] readChambres = restClient.get().uri("http://localhost:8081/api/chambre/all?proprietaireId="+userId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, clientResponse) -> {
                        if (clientResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "chambres non trouvé");
                        }
                        System.out.println("bad request okay");
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Erreur dans la requête");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Erreur serveur lors de la recuperation");
                    })
                    .body(ReadChambreDTO[].class);
            assert readChambres != null;
            List<Long> chambreIds = Arrays.stream(readChambres).map(ReadChambreDTO::id).toList();
            contratSearchCriteria.setChambreIds(chambreIds);
        }

        Specification<Contrat> spec = ContratSpecification.withCriteria(contratSearchCriteria);
        Page<Contrat> entities = contratRepository.findAll(spec, pageable);
        return entities.map(Contrat::toInfoContratResponse);
    }

    @Override
    public InfoContratResponse saveContrat(CreateContratRequest createContratRequest) {
        // Importer java.math.RoundingMode si pas déjà fait

        // Créer une nouvelle instance de Contrat
        Contrat contrat = new Contrat()
                .setLocataireId(createContratRequest.locataireId())
                .setChambreId(createContratRequest.chambreId())
                .setDateDebut(createContratRequest.dateDebut().toLocalDate())
                .setDateFin(createContratRequest.dateFin().toLocalDate())
                .setMontantCaution(BigDecimal.valueOf(createContratRequest.montantCaution()))
                .setMoisCaution(createContratRequest.moisCaution())
                .setDescription(createContratRequest.description())
                .setModePaiement(createContratRequest.modePaiement())
                .setPeriodicite(createContratRequest.periodicite())
                .setStatut(createContratRequest.statut());

        // Sauvegarder le contrat d'abord pour obtenir l'ID
        Contrat contratSauvegarde = contratRepository.save(contrat);

        // Générer les paiements programmés
        List<Paiement> paiements = genererPaiementsProgrammes(contratSauvegarde);
        contratSauvegarde.setPaiements(paiements);

        // Sauvegarder les paiements (ils seront persistés automatiquement grâce à CascadeType.ALL)
        contratRepository.save(contratSauvegarde);

        // Convertir et retourner la réponse
        return Contrat.toInfoContratResponse(contratSauvegarde);
    }

    @Override
    public InfoContratWChambreAndLocataireResponse getContratById(Long contratId) {
        if (contratId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les paramètres sont obligatoires");
        }

        Optional<Contrat> contratOptional = contratRepository.findById(contratId);
        if (contratOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chambre introuvable");
        Contrat contrat = contratOptional.get();

        RestClient UtilisateurClient = RestClient.create(gatewayServiceUrl);
        RestClient LogementClient = RestClient.create(logementServiceUrl);

        ReadUtilisateurDTO utilisateurDTO = UtilisateurClient.get().uri("http://localhost:8080/api/user/" + contrat.locataireId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, clientResponse) -> {
                    if (clientResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Utilisateur non trouvé");
                    }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Erreur dans la requête");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Erreur serveur lors de la recuperation");
                })
                .body(ReadUtilisateurDTO.class);

        ReadChambreDTO chambreDTO = LogementClient.get().uri("http://localhost:8081/api/chambre/" + contrat.chambreId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, clientResponse) -> {
                    if (clientResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "chambre non trouvé");
                    }
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Erreur dans la requête");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Erreur serveur lors de la recuperation");
                })
                .body(ReadChambreDTO.class);

        return Contrat.toInfoContratWChambreAndLocataireResponse(contrat, chambreDTO, utilisateurDTO);
    }

    @Override
    public InfoContratResponse updateContrat(Long contratId, UpdateContratRequest updateContratRequest) {
        Contrat contrat = contratRepository.findById(contratId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contrat non trouvé avec l'ID: " + contratId));


        // Mise à jour conditionnelle des autres champs
        if (updateContratRequest.dateDebut() != null) {
            contrat.setDateDebut(updateContratRequest.dateDebut());
        }

        if (updateContratRequest.dateFin() != null) {
            contrat.setDateFin(updateContratRequest.dateFin());
        }

        if (updateContratRequest.montantCaution() != null) {
            contrat.setMontantCaution(updateContratRequest.montantCaution());
        }

        if (updateContratRequest.moisCaution() != null) {
            contrat.setMoisCaution(updateContratRequest.moisCaution());
        }

        if (updateContratRequest.description() != null && !updateContratRequest.description().trim().isEmpty()) {
            contrat.setDescription(updateContratRequest.description().trim());
        }

        if (updateContratRequest.modePaiement() != null) {
            contrat.setModePaiement(updateContratRequest.modePaiement());
        }

        if (updateContratRequest.periodicite() != null) {
            contrat.setPeriodicite(updateContratRequest.periodicite());
        }

        if (updateContratRequest.statut() != null) {
            // Gestion spéciale pour la résiliation
            contrat.setStatut(updateContratRequest.statut());
        }

        Contrat contratUpdated = contratRepository.save(contrat);

        return Contrat.toInfoContratResponse(contratUpdated);
    }

    private List<Paiement> genererPaiementsProgrammes(Contrat contrat) {
        List<Paiement> paiements = new ArrayList<>();
        LocalDate dateDebut = contrat.dateDebut();
        LocalDate dateFin = contrat.dateFin();
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDate dateDebutLoyer = dateDebut;

        // 1. Créer le paiement de caution (à la date de début du contrat)
        if (contrat.periodicite() == Contrat.Periodicite.MENSUEL) {
            Paiement paiementCaution = new Paiement()
                    .setContrat(contrat)
                    .setContratId(contrat.id())
                    .setMontant(contrat.montantCaution())
                    .setDateEcheance(dateDebut)
                    .setStatut(Paiement.Statut.IMPAYE)
                    .setCreeLe(maintenant);
            paiements.add(paiementCaution);

            dateDebutLoyer = dateDebut.plusMonths(contrat.moisCaution());
        }

        // 2. Calculer le montant du loyer mensuel basé sur la caution et les mois de caution
        BigDecimal montantLoyerMensuel = contrat.montantCaution().divide(
                BigDecimal.valueOf(contrat.moisCaution()), 2, RoundingMode.HALF_UP);

        // 4. Générer les paiements de loyer selon la périodicité
        LocalDate datePaiementCourante = dateDebutLoyer;

        BigDecimal montantPaiement = calculerMontantSelonPeriodicite(montantLoyerMensuel, contrat.periodicite());

        while (!datePaiementCourante.isAfter(dateFin)) {
            // Calculer le montant selon la périodicité


            Paiement paiementLoyer = new Paiement()
                    .setContrat(contrat)
                    .setContratId(contrat.id())
                    .setMontant(montantPaiement)
                    .setDateEcheance(datePaiementCourante)
                    .setStatut(Paiement.Statut.IMPAYE)
                    .setCreeLe(maintenant);
            paiements.add(paiementLoyer);

            // Calculer la prochaine date selon la périodicité
            datePaiementCourante = calculerProchainePeriode(datePaiementCourante, contrat.periodicite());
        }

        return paiements;
    }

    private BigDecimal calculerMontantSelonPeriodicite(BigDecimal montantMensuel, Contrat.Periodicite periodicite) {
        return switch (periodicite) {
            case JOURNALIER -> montantMensuel.divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
            case HEBDOMADAIRE -> montantMensuel.divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
            case MENSUEL -> montantMensuel;
        };
    }

    private LocalDate calculerProchainePeriode(LocalDate dateActuelle, Contrat.Periodicite periodicite) {
        return switch (periodicite) {
            case JOURNALIER -> dateActuelle.plusDays(1);
            case HEBDOMADAIRE -> dateActuelle.plusWeeks(1);
            case MENSUEL -> dateActuelle.plusMonths(1);
        };
    }


}
