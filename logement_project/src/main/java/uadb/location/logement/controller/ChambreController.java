package uadb.location.logement.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import uadb.location.logement.controller.Interfaces.IChambreController;
import uadb.location.logement.dto.controller.chambreController.InfoChambreResponse;
import uadb.location.logement.dto.controller.chambreController.createChambre.CreateChambreRequest;
import uadb.location.logement.dto.controller.chambreController.updateChambre.UpdateChambreRequest;
import uadb.location.logement.dto.controller.mediaController.InfoMediaResponse;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.Chambre.ChambreSearchCriteria;
import uadb.location.logement.services.ChambreService;
import uadb.location.logement.services.MediaService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/chambre")
@RestController
public class ChambreController implements IChambreController {

    private final ChambreService chambreService;

    public ChambreController(ChambreService chambreService) {
        this.chambreService = chambreService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoChambreResponse>> readChambres(
            @PageableDefault Pageable pageable,
            @ModelAttribute ChambreSearchCriteria chambreSearchCriteria,
            HttpServletRequest request
    ) {

        Cookie id = WebUtils.getCookie(request, "user_id");

        if (id != null) {
            Long userId = Long.parseLong(id.getValue());
            chambreSearchCriteria.setProprietaireId(userId);
        }
        return ResponseEntity.ok(chambreService.getAllChambres(pageable, chambreSearchCriteria));
    }

    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InfoChambreResponse>> readChambreWOPage(
            @ModelAttribute ChambreSearchCriteria chambreSearchCriteria,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(chambreService.getAllChambresWOPage(chambreSearchCriteria));
    }

    @GetMapping(value = "/visiteur", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoChambreResponse>> readChambresVisiteur(
            @PageableDefault Pageable pageable,
            @ModelAttribute ChambreSearchCriteria chambreSearchCriteria
    ) {
        return ResponseEntity.ok(chambreService.getAllChambres(pageable, chambreSearchCriteria));
    }

    @GetMapping(value = "/{chambreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoChambreResponse> readChambre(
            @PathVariable @NotNull Long chambreId,
            HttpServletRequest request
    ) {
        Long userId = null;
        Cookie id = WebUtils.getCookie(request, "user_id");
        if (id != null) {
            userId = Long.parseLong(id.getValue());
        }


        return ResponseEntity.ok(Chambre.toChambreResponse(chambreService.getChambreById(userId, chambreId)));
    }

    @PostMapping
    public ResponseEntity<InfoChambreResponse> createChambre(
            @RequestBody CreateChambreRequest createChambreRequest,
            HttpServletRequest request
    ) {
        try {
            /*Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());*/

            return ResponseEntity.ok(chambreService.saveChambre(createChambreRequest));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !" + e);
        }
    }


    @PutMapping("/{chambreId}")
    public ResponseEntity<InfoChambreResponse> updateChambre(
            @PathVariable Long chambreId,
            @Valid @RequestBody UpdateChambreRequest updateChambreRequest, HttpServletRequest request
    ) {

        Cookie id = WebUtils.getCookie(request, "user_id");
        assert id != null;
        Long userId = Long.parseLong(id.getValue());

        InfoChambreResponse updatedChambre = chambreService.updateChambre(
                updateChambreRequest,
                userId);

        return ResponseEntity.ok(updatedChambre);
    }

    @DeleteMapping("/{chambreId}")
    public ResponseEntity<String> deleteMaison(
            @PathVariable Long chambreId,
            HttpServletRequest request
    ) {
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());

            chambreService.deleteChambre(userId, chambreId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !");
        }
    }
}
