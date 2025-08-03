package uadb.location.logement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.logement.dto.client.mediaClient.CreateMediaDto;
import uadb.location.logement.dto.controller.mediaController.InfoMediaResponse;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.Media;
import uadb.location.logement.repositories.ChambreRepository;
import uadb.location.logement.repositories.MediaRepository;
import uadb.location.logement.services.Interfaces.IMediaService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService implements IMediaService {

    @Value("${cdn.service.url}")
    private String cdnServiceUrl;

    private final MediaRepository mediaRepository;
    private final ChambreRepository chambreRepository;


    public MediaService(
            MediaRepository mediaRepository,
            ChambreRepository chambreRepository
    ) {
        this.mediaRepository = mediaRepository;
        this.chambreRepository = chambreRepository;
    }

    public List<InfoMediaResponse> createFiles(
            MultipartFile[] files,
            Long proprietaireId,
            Long chambreId
    ) {
        try {

            RestClient restClient = RestClient.create(cdnServiceUrl);

            MultiValueMap<String, Resource> body = new LinkedMultiValueMap<>();

            Arrays.stream(files).forEach((file) -> {
                body.add("files", file.getResource());
            });

            CreateMediaDto[] mediaDtoList = restClient.post()
                    .uri("/api/cdn/" + proprietaireId + "/" + chambreId)
                    .body(body)
                    .retrieve()
                    .body(CreateMediaDto[].class);

            Chambre chambre = chambreRepository
                    .findById(chambreId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chambre introuvable"));

            List<Media> savedMedias = new ArrayList<>();

            if (mediaDtoList != null) {
                for (CreateMediaDto mediaDto : mediaDtoList) {
                    Media media = new Media();
                    media.setUrl(mediaDto.fullMediaName());
                    media.setType(detectMediaType(mediaDto.fullMediaName()));
                    media.setDescription("Fichier uploadé par l'utilisateur " + proprietaireId);
                    media.setChambre(chambre);
                    savedMedias.add(media);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "information manquante dans la requete ! ");
            }


            return ((List<Media>) mediaRepository.saveAll(savedMedias))
                    .stream()
                    .map(Media::toInfoMediaResponse)
                    .toList();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !: " + e.getMessage());
        }
    }

    public static Media.Type detectMediaType(
            String url
    ) {
        if (url == null || !url.contains(".")) {
            return Media.Type.PHOTO; // Par défaut
        }

        String extension = url.substring(url.lastIndexOf('.') + 1).toLowerCase();

        List<String> imageExtensions = List.of("jpg", "jpeg", "png", "gif", "bmp", "webp");
        List<String> videoExtensions = List.of("mp4", "avi", "mov", "mkv", "webm", "flv");

        if (imageExtensions.contains(extension)) {
            return Media.Type.PHOTO;
        } else if (videoExtensions.contains(extension)) {
            return Media.Type.VIDEO;
        } else {
            return Media.Type.PHOTO; // Fallback
        }
    }

    @Override
    public InfoMediaResponse updateMedia(Long userId, Long chambreId, Long mediaId, String description) {
        boolean chambreExists = chambreRepository.existsByIdAndMaison_ProprietaireId(chambreId, userId);

        if (!chambreExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operation Impossible");
        }

        Optional<Media> mediaOptional = mediaRepository.findById(mediaId);
        if (mediaOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media Introuvable");
        }
        Media media = mediaOptional.get();

        media.setDescription(description);
        Media newMedia = mediaRepository.save(media);

        return Media.toInfoMediaResponse(media);
    }

    @Override
    public void deleteMedia(Long proprietaireId, Long chambreId, Long mediaId) {
        try {

            Optional<Media> mediaOptional = mediaRepository.findById(mediaId);

            if (mediaOptional.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le media est introuvable");
            }
            Media media = mediaOptional.get();

            URI uri = new URI(media.getUrl());


            RestClient restClient = RestClient.create(cdnServiceUrl);

            ResponseEntity<Void> response = restClient.delete().uri(uri)
                    .retrieve()
                    .toBodilessEntity();

            HttpStatusCode status = response.getStatusCode();
            if (!status.is2xxSuccessful()){
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "un probleme est survenu lors de la supression");
            }

            mediaRepository.deleteById(mediaId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !: " + e.getMessage());
        }
    }
}
