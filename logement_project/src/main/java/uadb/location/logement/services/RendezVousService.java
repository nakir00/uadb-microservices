package uadb.location.logement.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.logement.dto.client.UtilisateurClient.ReadUtilisateurDTO;
import uadb.location.logement.dto.controller.rendezVousController.InfoRendezVousResponse;
import uadb.location.logement.dto.controller.rendezVousController.UpdateRendezVousRequest.UpdateRendezVousRequest;
import uadb.location.logement.dto.controller.rendezVousController.createRendezVousRequest.CreateRendezVousRequest;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.Chambre.ChambreSpecifications;
import uadb.location.logement.model.maison.Maison;
import uadb.location.logement.model.rendezVous.RendezVous;
import uadb.location.logement.model.rendezVous.RendezVousSearchCriteria;
import uadb.location.logement.model.rendezVous.RendezVousSpecifications;
import uadb.location.logement.repositories.ChambreRepository;
import uadb.location.logement.repositories.RendezVousRepository;
import uadb.location.logement.services.Interfaces.IRendezVousService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RendezVousService implements IRendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final ChambreRepository chambreRepository;
    @Value("${gateway.service.url}")
    private String gatewayServiceUrl;

    public RendezVousService(RendezVousRepository rendezVousRepository, ChambreRepository chambreRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.chambreRepository = chambreRepository;
    }

    @Override
    public Page<InfoRendezVousResponse> getAllRendezVous(Pageable pageable, RendezVousSearchCriteria rendezVousSearchCriteria) {
        Specification<RendezVous> spec = RendezVousSpecifications.withCriteria(rendezVousSearchCriteria);
        Page<RendezVous> entities = rendezVousRepository.findAll(spec, pageable);
        return entities.map(RendezVous::toInfoRendezVousResponse);
    }

    @Override
    public InfoRendezVousResponse saveRendezVous(CreateRendezVousRequest createRendezVousRequest) {
        Long id = createRendezVousRequest.chambreId();
        Optional<Chambre> chambreOptional = chambreRepository.findById(id);
        if (chambreOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'id de la maison n'existe pas");
        }
        Chambre chambre = chambreOptional.get();


        RendezVous rendezVous = RendezVous.fromCreateRendezVousRequest(createRendezVousRequest);

        rendezVous.setChambre(chambre);
        RendezVous rendezVous1 = rendezVousRepository.save(rendezVous);

        return RendezVous.toInfoRendezVousResponse(rendezVous1);
    }

    @Transactional
    public InfoRendezVousResponse updateRendezVous(
            @NotNull UpdateRendezVousRequest updateRendezVousRequest,
            Long rendezVousId
    ) {

        // Validation des paramètres obligatoires
        if (rendezVousId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'ID du rendez-vous est obligatoire");
        }

        // Récupération du rendez-vous existant
        Optional<RendezVous> rendezVousOpt = rendezVousRepository.findById(rendezVousId);
        if (rendezVousOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendez-vous introuvable");
        }

        RendezVous existingRendezVous = rendezVousOpt.get();

        // Validation de la chambre si elle est modifiée
        Chambre chambre = existingRendezVous.chambre();
        if (updateRendezVousRequest.chambreId() != null &&
                !updateRendezVousRequest.chambreId().equals(existingRendezVous.chambre().getId())) {

            Optional<Chambre> newChambreOpt = chambreRepository.findById(updateRendezVousRequest.chambreId());
            if (newChambreOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chambre introuvable avec l'ID: " + updateRendezVousRequest.chambreId());
            }

            chambre = newChambreOpt.get();

            // Vérifier que la chambre est disponible
            if (!chambre.disponible()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La chambre sélectionnée n'est pas disponible");
            }
        }

        // Validations métier
        validateRendezVousUpdate(updateRendezVousRequest, existingRendezVous);

        // Construction du rendez-vous mis à jour
        RendezVous updatedRendezVous = new RendezVous(
                existingRendezVous.id(),
                updateRendezVousRequest.locataireId() != null ?
                        updateRendezVousRequest.locataireId() : existingRendezVous.locataireId(),
                updateRendezVousRequest.dateHeure() != null ?
                        updateRendezVousRequest.dateHeure() : existingRendezVous.dateHeure(),
                updateRendezVousRequest.statut() != null ?
                        updateRendezVousRequest.statut() : existingRendezVous.statut(),
                existingRendezVous.creeLe(),
                chambre
        );

        // Sauvegarde et retour de la réponse
        RendezVous savedRendezVous = rendezVousRepository.save(updatedRendezVous);
        return RendezVous.toInfoRendezVousResponse(savedRendezVous);
    }

    @Override
    public RendezVous getRendezVousById(Long rendezVousId, Long userId) {
        if (rendezVousId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les paramètres sont obligatoires");
        }

        Optional<RendezVous> rendezVousOptional = rendezVousRepository.findByIdWithChambre(rendezVousId);

        if (rendezVousOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendez-vous introuvable");
        }

        RendezVous rendezVous = rendezVousOptional.get();
        Chambre chambre = rendezVous.chambre();

        if (chambre != null) {
            Maison maison = chambre.getMaison();

            if (maison != null) {
                // Récupération des informations du propriétaire via l'API Gateway
                RestClient restClient = RestClient.create(gatewayServiceUrl);

                ReadUtilisateurDTO utilisateurDTO = restClient.get()
                        .uri("http://localhost:8080/api/user/" + maison.getProprietaireId())
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, (request, clientResponse) -> {
                            if (clientResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Propriétaire non trouvé");
                            }
                            System.out.println("bad request okay");
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                    "Erreur dans la requête");
                        })
                        .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Erreur serveur lors de la récupération du propriétaire");
                        })
                        .body(ReadUtilisateurDTO.class);

                maison.setUtilisateurDTO(utilisateurDTO);
                chambre.setMaison(maison);
                rendezVous.setChambre(chambre);
            }
        }

        return rendezVous;
    }

    /**
     * Méthode privée pour valider les données de mise à jour du rendez-vous
     */
    private void validateRendezVousUpdate(UpdateRendezVousRequest request, RendezVous existing) {

        // Validation de la date/heure
        if (request.dateHeure() != null) {
            LocalDateTime now = LocalDateTime.now();

            // Ne pas permettre de planifier dans le passé
            if (request.dateHeure().isBefore(now)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La date et l'heure du rendez-vous ne peuvent pas être dans le passé");
            }

            // Ne pas permettre de planifier trop loin dans le futur (ex: 6 mois)
            if (request.dateHeure().isAfter(now.plusMonths(6))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La date du rendez-vous ne peut pas être planifiée au-delà de 6 mois");
            }

            // Vérifier les heures ouvrables (ex: 8h-18h)
            int hour = request.dateHeure().getHour();
            if (hour < 8 || hour >= 18) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Les rendez-vous ne peuvent être planifiés qu'entre 8h et 18h");
            }
        }

        // Validation du changement de statut
        if (request.statut() != null) {
            validateStatutChange(existing.statut(), request.statut());
        }

        // Validation du locataire
        if (request.locataireId() != null) {
            // Ici vous pourriez vérifier que le locataire existe
            // Optional<Utilisateur> locataire = utilisateurRepository.findById(request.locataireId());
            // if (locataire.isEmpty() || !locataire.get().getRole().equals("ROLE_LOCATAIRE")) {
            //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Locataire invalide");
            // }
        }
    }

    /**
     * Méthode privée pour valider les transitions de statut
     */
    private void validateStatutChange(RendezVous.Statut currentStatut, RendezVous.Statut newStatut) {
        // Règles métier pour les transitions de statut
        switch (currentStatut) {
            case EN_ATTENTE:
                // Depuis EN_ATTENTE, on peut aller vers CONFIRME ou ANNULE
                if (newStatut != RendezVous.Statut.CONFIRME && newStatut != RendezVous.Statut.ANNULE) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Un rendez-vous en attente ne peut être que confirmé ou annulé");
                }
                break;

            case CONFIRME:
                // Depuis CONFIRME, on peut seulement aller vers ANNULE
                if (newStatut != RendezVous.Statut.ANNULE) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Un rendez-vous confirmé ne peut être qu'annulé");
                }
                break;

            case ANNULE:
                // Un rendez-vous annulé ne peut plus changer de statut
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Un rendez-vous annulé ne peut plus être modifié");
        }
    }
}
