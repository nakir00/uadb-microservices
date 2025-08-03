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
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.contrat.ContratSpecification;
import uadb.location.contrat.model.paiement.Paiement;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.model.paiement.PaiementSpecification;
import uadb.location.contrat.repositories.ContratRepository;
import uadb.location.contrat.repositories.PaiementRepository;
import uadb.location.contrat.service.Interface.IContratService;
import uadb.location.contrat.service.Interface.IPaiementService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class PaiementService implements IPaiementService {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;

    @Value("${gateway.service.url}")
    private String gatewayServiceUrl;

    @Value("${logement.service.url}")
    private String logementServiceUrl;

    public PaiementService(PaiementRepository paiementRepository, ContratRepository contratRepository) {
        this.paiementRepository = paiementRepository;
        this.contratRepository = contratRepository;
    }

    @Override
    public Page<InfoPaiementResponse> getAllPaiements(Pageable pageable, PaiementSearchCriteria paiementSearchCriteria) {
        Specification<Paiement> spec = PaiementSpecification.withCriteria(paiementSearchCriteria);
        Page<Paiement> entities = paiementRepository.findAll(spec, pageable);
        return entities.map(Paiement::toInfoPaiementResponse);
    }

    @Override
    public InfoPaiementResponse payer(Long paiementId, Long userId) {
        // Récupérer le paiement
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paiement non trouvé avec l'ID : " + paiementId));

        // Vérifier que le paiement est en attente (IMPAYE)
        if (paiement.statut() == Paiement.Statut.PAYE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le paiement est déjà marqué comme payé");
        }

        // Récupérer le contrat associé
        Contrat contrat = paiement.contrat();
        if (contrat == null) {
            // Si la relation contrat n'est pas chargée, récupérer via contratRepository
            contrat = contratRepository.findById(paiement.contratId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrat non trouvé avec l'ID : " + paiement.contratId()));
        }

        // Vérifier que l'utilisateur est le locataire du contrat
        if (!contrat.locataireId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seul le locataire du contrat peut effectuer ce paiement");
        }

        // Mettre à jour le paiement
        paiement.setStatut(Paiement.Statut.PAYE)
                .setDatePaiement(LocalDateTime.now());

        // Sauvegarder les modifications
        paiementRepository.save(paiement);

        // Retourner la réponse
        return Paiement.toInfoPaiementResponse(paiement);
    }

    @Override
    public List<InfoPaiementResponse> getAllPaiementsAll(Long userId, PaiementSearchCriteria paiementSearchCriteria) {
        RestClient restClient = RestClient.create(logementServiceUrl);
        ReadChambreDTO[] readChambres = restClient.get().uri("http://localhost:8081/api/chambre/all?proprietaireId="+ userId)
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
        paiementSearchCriteria.setChambreIds(chambreIds);
        Specification<Paiement> spec = PaiementSpecification.withCriteria(paiementSearchCriteria);
        List<Paiement> entities = paiementRepository.findAll(spec);
        return entities.stream().map(Paiement::toInfoPaiementResponse).toList();
    }

}
