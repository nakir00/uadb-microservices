package uadb.location.logement.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.logement.client.UtilisateurClient;
import uadb.location.logement.dto.client.UtilisateurClient.ReadUtilisateurDTO;
import uadb.location.logement.dto.client.mediaClient.CreateMediaDto;
import uadb.location.logement.dto.controller.chambreController.InfoChambreResponse;
import uadb.location.logement.dto.controller.chambreController.createChambre.CreateChambreRequest;
import uadb.location.logement.dto.controller.chambreController.updateChambre.UpdateChambreRequest;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.Chambre.ChambreSearchCriteria;
import uadb.location.logement.model.Chambre.ChambreSpecifications;
import uadb.location.logement.model.Media;
import uadb.location.logement.model.maison.Maison;
import uadb.location.logement.repositories.ChambreRepository;
import uadb.location.logement.repositories.MaisonRepository;
import uadb.location.logement.repositories.MediaRepository;
import uadb.location.logement.repositories.RendezVousRepository;
import uadb.location.logement.services.Interfaces.IChambreService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChambreService implements IChambreService {

    private final ChambreRepository chambreRepository;
    private final MaisonRepository maisonRepository;
    private final RendezVousRepository rendezVousRepository;
    private final MediaRepository mediaRepository;
    private final UtilisateurClient utilisateurClient;
    @Value("${gateway.service.url}")
    private String gatewayServiceUrl;

    public ChambreService(ChambreRepository chambreRepository, MaisonRepository maisonRepository, RendezVousRepository rendezVousRepository, MediaRepository mediaRepository, UtilisateurClient utilisateurClient) {
        this.chambreRepository = chambreRepository;
        this.maisonRepository = maisonRepository;
        this.rendezVousRepository = rendezVousRepository;
        this.mediaRepository = mediaRepository;
        this.utilisateurClient = utilisateurClient;
    }


    public Page<InfoChambreResponse> getAllChambres(
            Pageable pageable,
            ChambreSearchCriteria chambreSearchCriteria
    ) {
        Specification<Chambre> spec = ChambreSpecifications.withCriteria(chambreSearchCriteria);
        Page<Chambre> entities = chambreRepository.findAll(spec, pageable);
        return entities.map(Chambre::toChambreResponse);
    }

    @Transactional
    public InfoChambreResponse saveChambre(@NotNull CreateChambreRequest createChambreRequest) {
        Long id = createChambreRequest.maisonId();
        Optional<Maison> maisonOptional = maisonRepository.findById(id);
        if (maisonOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "L'id de la maison n'existe pas");
        }
        Maison maison = maisonOptional.get();


        Chambre chambre = Chambre.fromCreateChambreRequest(createChambreRequest);

        chambre.setMaison(maison);
        Chambre savedChambre = chambreRepository.save(chambre);

        return Chambre.toChambreResponse(savedChambre);
    }

    @Transactional
    public InfoChambreResponse updateChambre(
            @NotNull UpdateChambreRequest updateChambreRequest,
            Long proprietaireId
    ) {

        if (proprietaireId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tous les paramètres sont obligatoires");
        }

        Optional<Chambre> chambreOpt = chambreRepository.findById(updateChambreRequest.id());
        if (chambreOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chambre introuvable");
        }
        Maison chambreMaison = chambreOpt.get().getMaison();
        if (chambreMaison == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maison de la chambre introuvable");
        }


        Chambre existingChambre = chambreOpt.get();

        Chambre updatedChambre = new Chambre(
                chambreOpt.get().id(),
                updateChambreRequest.titre(),
                updateChambreRequest.description(),
                updateChambreRequest.taille(),
                updateChambreRequest.type(),
                updateChambreRequest.meublee(),
                updateChambreRequest.salleDeBain(),
                updateChambreRequest.disponible(),
                updateChambreRequest.prix(),
                existingChambre.creeLe(),
                chambreOpt.get().maison(),
                existingChambre.medias(),
                existingChambre.rendezVousList()
        );

        return Chambre.toChambreResponse(chambreRepository.save(updatedChambre));
    }


    @Override
    @Transactional
    public void deleteChambre(Long proprietaireId, Long chambreId) {
        if (proprietaireId == null || chambreId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les paramètres sont obligatoires");
        }

        // Vérifier si la chambre existe et appartient au propriétaire
        boolean chambreExists = chambreRepository.existsByIdAndMaison_ProprietaireId(chambreId, proprietaireId);

        if (!chambreExists) {
            // Vérifier si la chambre existe pour un autre propriétaire
            boolean chambreExistsForOtherOwner = chambreRepository.existsById(chambreId);
            if (chambreExistsForOtherOwner) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous n'avez pas le droit de supprimer cette chambre");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chambre introuvable");
            }
        }

        try {
            // Supprimer toutes les données liées à cette chambre
            deleteAllChambreRelatedDataBulk(chambreId);

            // Supprimer la chambre
            chambreRepository.deleteById(chambreId);

            //log.info("Chambre supprimée avec succès - ID : {}, Propriétaire : {}", chambreId, proprietaireId);

        } catch (Exception e) {
            //log.error("Erreur lors de la suppression de la chambre {}: {}", chambreId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression");
        }
    }


    @Override
    public Chambre getChambreById(@Nullable Long userId, Long chambreId) {
        if (chambreId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les paramètres sont obligatoires");
        }

        Optional<Chambre> chambreOptional = chambreRepository.findByIdWithMaison(chambreId);
        if (chambreOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chambre introuvable");
        Chambre chambre = chambreOptional.get();

        Maison maison = chambre.getMaison();


        RestClient restClient = RestClient.create(gatewayServiceUrl);

        ReadUtilisateurDTO utilisateurDTO = restClient.get().uri("http://localhost:8080/api/user/"+ maison.getProprietaireId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, clientResponse) -> {
                    if (clientResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Utilisateur non trouvé");
                    }
                    System.out.println("bad request okay");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Erreur dans la requête");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, clientResponse) -> {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Erreur serveur lors de la suppression");
                })
                .body(ReadUtilisateurDTO.class);



        maison.setUtilisateurDTO(utilisateurDTO);
        chambre.setMaison(maison);

        return chambre;
    }

    @Override
    public List<InfoChambreResponse> getAllChambresWOPage(ChambreSearchCriteria chambreSearchCriteria) {
        Specification<Chambre> spec = ChambreSpecifications.withCriteria(chambreSearchCriteria);
        List<Chambre> entities = chambreRepository.findAll(spec);
        return entities.stream().map(Chambre::toChambreResponse).toList();
    }

    private void deleteAllChambreRelatedDataBulk(Long chambreId) {

        try {
            // Supprimer les rendez-vous liés à cette chambre
            rendezVousRepository.deleteByChambreId(chambreId);

            // Supprimer les médias liés à cette chambre
            mediaRepository.deleteByChambreId(chambreId);

            // Vous pouvez ajouter d'autres suppressions selon vos besoins
            // Par exemple : commentaires, favoris, etc.

        } catch (Exception e) {
            log.error("Erreur lors de la suppression des données liées à la chambre {}: {}", chambreId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression des données liées");
        }
    }
}
