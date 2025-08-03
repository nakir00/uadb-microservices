package uadb.location.logement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import uadb.location.logement.controller.Interfaces.IMaisonController;
import uadb.location.logement.dto.controller.maisonController.createMaison.CreateMaisonRequest;
import uadb.location.logement.dto.controller.maisonController.InfoMaisonResponse;
import uadb.location.logement.dto.controller.maisonController.updateMaison.UpdateMaisonRequest;
import uadb.location.logement.model.maison.Maison;
import uadb.location.logement.model.maison.MaisonSearchCriteria;
import uadb.location.logement.services.MaisonService;

import java.util.Optional;

@RestController
@RequestMapping("/api/maison")
@Tag(name = "Management des Maisons", description = "l'ensemble des endpoints sur les maisons")
public class MaisonController implements IMaisonController {

    private final MaisonService maisonService;

    public MaisonController(MaisonService maisonService) {
        this.maisonService = maisonService;
    }

    @GetMapping
    public ResponseEntity<Page<InfoMaisonResponse>> readMaisons(
            @PageableDefault Pageable pageable,
            @ModelAttribute MaisonSearchCriteria maisonSearchCriteria
            ) {
        /*Cookie id = WebUtils.getCookie(request, "user_id");
        assert id != null;
        Long userId = Long.parseLong(id.getValue());*/
        return ResponseEntity.ok(maisonService.getAllMaisons(pageable, maisonSearchCriteria));
    }


    @GetMapping(value = "/{maisonId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Maison> readMaison(
            @PathVariable @NotNull Long maisonId,
            HttpServletRequest request
    ) {
        Cookie id = WebUtils.getCookie(request, "user_id");
        assert id != null;
        Long userId = Long.parseLong(id.getValue());
        System.out.println(maisonId.toString());
        Optional<Maison> maison = maisonService.getMaisonById( userId, maisonId);
        if(maison.isPresent()){
            System.out.println("maison is present");
            Maison found = maison.get();
            return ResponseEntity.ok( found);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maison introuvable");
        }

    }



    @PostMapping
    public ResponseEntity<InfoMaisonResponse> createMaison(@RequestBody CreateMaisonRequest infoMaisonRequest, HttpServletRequest request) {
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());
            Maison maison = maisonService.CreateMaison(infoMaisonRequest, userId);
            return ResponseEntity.ok(Maison.toInfoMaisonResponse(maison));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !");
        }
    }

    @PutMapping("/{maisonId}")
    public ResponseEntity<InfoMaisonResponse> updateMaison(
            @PathVariable Long maisonId,
            @RequestBody UpdateMaisonRequest maisonRequest,
            HttpServletRequest request) {
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());

            Maison maison = maisonService.UpdateMaison(maisonRequest, userId, maisonId);
            return ResponseEntity.ok(Maison.toInfoMaisonResponse(maison));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !");
        }
    }

    @DeleteMapping("/{maisonId}")
    public ResponseEntity<String> deleteMaison(
            @PathVariable Long maisonId,
            HttpServletRequest request) {
        System.out.println("delete");
        try {
            Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());

            maisonService.deleteMaisonOptimized(userId, maisonId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !");
        }
    }

}
