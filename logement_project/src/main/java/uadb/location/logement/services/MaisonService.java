package uadb.location.logement.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.logement.client.UtilisateurClient;
import uadb.location.logement.dto.controller.maisonController.InfoMaisonResponse;
import uadb.location.logement.dto.controller.maisonController.createMaison.CreateMaisonRequest;
import uadb.location.logement.dto.controller.maisonController.updateMaison.UpdateMaisonRequest;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.maison.Maison;
import uadb.location.logement.model.maison.MaisonSearchCriteria;
import uadb.location.logement.model.maison.MaisonSpecifications;
import uadb.location.logement.repositories.ChambreRepository;
import uadb.location.logement.repositories.MaisonRepository;
import uadb.location.logement.repositories.MediaRepository;
import uadb.location.logement.repositories.RendezVousRepository;
import uadb.location.logement.services.Interfaces.IMaisonService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaisonService implements IMaisonService {

    private final MaisonRepository maisonRepository;
    private final ChambreRepository chambreRepository;
    private final MediaRepository mediaRepository;
    private final RendezVousRepository rendezVousRepository;
    private final UtilisateurClient utilisateurClient;

    public MaisonService(MaisonRepository maisonRepository, ChambreRepository chambreRepository, MediaRepository mediaRepository, RendezVousRepository rendezVousRepository, UtilisateurClient utilisateurClient) {
        this.maisonRepository = maisonRepository;
        this.chambreRepository = chambreRepository;
        this.mediaRepository = mediaRepository;
        this.rendezVousRepository = rendezVousRepository;
        this.utilisateurClient = utilisateurClient;
    }


    public Page<InfoMaisonResponse> getAllMaisons(Pageable pageable,
                                      MaisonSearchCriteria maisonSearchCriteria
    ) {

        Specification<Maison> spec = MaisonSpecifications.withCriteria(maisonSearchCriteria);
        Page<Maison> entities = maisonRepository.findAll(spec, pageable);
        return entities.map(Maison::toInfoMaisonResponse);

    }

    @Transactional
    public Optional<Maison> getMaisonById(
            Long proprietaireId, Long maisonId
    ) {
        if (proprietaireId == null || maisonId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les paramètres sont obligatoires");
        }

        /* boolean maisonExists = maisonRepository.existsByIdAndProprietaireId(maisonId, proprietaireId);

        if (!maisonExists) {
            System.out.println("maison n'existe pas");
            boolean maisonExistsForOtherOwner = maisonRepository.existsById(maisonId);
            if (maisonExistsForOtherOwner) {
                System.out.println("aucun droit");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'avez pas le droit de lire cette maison");
            } else {
                System.out.println("maison introuvable");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maison introuvable");
            }
        } */

        return maisonRepository.findById(maisonId);
    }

    public Maison CreateMaison(@NotNull CreateMaisonRequest infoMaisonRequest, Long proprietaireId) {
        Maison maison = new Maison(infoMaisonRequest.getDescription(), infoMaisonRequest.getLongitude(), infoMaisonRequest.getLatitude(), infoMaisonRequest.getAdresse(), proprietaireId);
        return maisonRepository.save(maison);
    }

    public Maison UpdateMaison(@NotNull UpdateMaisonRequest infoMaisonRequest, Long proprietaireId, Long maisonId) {
        Maison maison = new Maison(maisonId, proprietaireId, infoMaisonRequest.adresse(), infoMaisonRequest.latitude(), infoMaisonRequest.longitude(), infoMaisonRequest.description());
        return maisonRepository.save(maison);
    }

    @Transactional
    public void deleteMaisonOptimized(Long proprietaireId, Long maisonId) {
        if (proprietaireId == null || maisonId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les paramètres sont obligatoires");
        }

        boolean maisonExists = maisonRepository.existsByIdAndProprietaireId(maisonId, proprietaireId);

        if (!maisonExists) {
            boolean maisonExistsForOtherOwner = maisonRepository.existsById(maisonId);
            if (maisonExistsForOtherOwner) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'avez pas le droit de supprimer cette maison");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maison introuvable");
            }
        }

        try {

            deleteAllRelatedDataBulk(maisonId);

            maisonRepository.deleteById(maisonId);

            log.info("Maison supprimée avec succès - ID: {}, Propriétaire: {}", maisonId, proprietaireId);

        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la maison {}: {}", maisonId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression");
        }
    }

    private void deleteAllRelatedDataBulk(Long maisonId) {
        // Requêtes bulk plus efficaces
        List<Chambre> chambres = chambreRepository.findByMaisonId(maisonId);

        List<Long> chambreIds = chambres.stream()
                .map(Chambre::id)
                .collect(Collectors.toList());

        if (!chambreIds.isEmpty()) {
            mediaRepository.deleteByChambreIdIn(chambreIds);

            rendezVousRepository.deleteByChambreIdIn(chambreIds);
        }
    }
}
