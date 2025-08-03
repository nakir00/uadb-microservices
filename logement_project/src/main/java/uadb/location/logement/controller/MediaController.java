package uadb.location.logement.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import uadb.location.logement.client.CDNClient;
import uadb.location.logement.controller.Interfaces.IMediaController;
import uadb.location.logement.dto.client.mediaClient.CreateMediaDto;
import uadb.location.logement.dto.controller.maisonController.updateMaison.UpdateMaisonRequest;
import uadb.location.logement.dto.controller.mediaController.InfoMediaResponse;
import uadb.location.logement.dto.controller.mediaController.updateFiles.UpdateMediaRequest;
import uadb.location.logement.model.Media;
import uadb.location.logement.services.MediaService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/chambre/{chambreId}/media")
public class MediaController implements IMediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<List<InfoMediaResponse>> createFiles(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable("chambreId") Long chambreId,
            HttpServletRequest request
    ) {
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());

            return ResponseEntity.ok(mediaService.createFiles(files,userId,chambreId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !: " + e.getMessage());
        }
    }

    @PutMapping("/{mediaId}")
    public ResponseEntity<InfoMediaResponse> updateMedia(
            @PathVariable("chambreId") Long chambreId,
            @PathVariable("mediaId") Long mediaId,
            @RequestBody UpdateMediaRequest mediaRequest,
            HttpServletRequest request
    ) {
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());
            return ResponseEntity.ok(mediaService.updateMedia(userId,chambreId, mediaId, mediaRequest.description()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !: " + e.getMessage());
        }
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<?> deleteMedia(
            @PathVariable("chambreId") Long chambreId,
            @PathVariable("mediaId") Long mediaId,
            HttpServletRequest request
    ) {
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());
            mediaService.deleteMedia(userId,chambreId, mediaId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !: " + e.getMessage());
        }
    }


}
