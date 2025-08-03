package uadb.location.logement.services.Interfaces;

import uadb.location.logement.dto.controller.mediaController.InfoMediaResponse;

import java.util.List;

public interface IMediaService {
    InfoMediaResponse updateMedia(Long userId, Long chambreId, Long mediaId, String description);

    void deleteMedia(Long userId, Long chambreId, Long mediaId);
}
