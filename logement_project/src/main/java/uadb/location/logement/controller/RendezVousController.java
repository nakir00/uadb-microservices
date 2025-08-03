package uadb.location.logement.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import uadb.location.logement.controller.Interfaces.IRendezVousController;
import uadb.location.logement.dto.controller.rendezVousController.InfoRendezVousResponse;
import uadb.location.logement.dto.controller.rendezVousController.UpdateRendezVousRequest.UpdateRendezVousRequest;
import uadb.location.logement.dto.controller.rendezVousController.createRendezVousRequest.CreateRendezVousRequest;
import uadb.location.logement.model.rendezVous.RendezVous;
import uadb.location.logement.model.rendezVous.RendezVousSearchCriteria;
import uadb.location.logement.services.RendezVousService;

import java.util.Optional;

@RequestMapping("/api/rendez-vous")
@RestController
public class RendezVousController implements IRendezVousController {

    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoRendezVousResponse>> readReadRendezVous(
            @PageableDefault Pageable pageable,
            @ModelAttribute RendezVousSearchCriteria rendezVousSearchCriteria
    ) {
        return ResponseEntity.ok(rendezVousService.getAllRendezVous(pageable, rendezVousSearchCriteria));
    }

    @GetMapping(value = "/{rendezVousId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InfoRendezVousResponse> readMaison(
            @PathVariable("rendezVousId") @NotNull Long rendezVousId,
            HttpServletRequest request
    ) {
        Long userId = null;
        Cookie id = WebUtils.getCookie(request, "user_id");
        if (id != null) {
            userId = Long.parseLong(id.getValue());
        }


        return ResponseEntity.ok(RendezVous.toInfoRendezVousResponse(rendezVousService.getRendezVousById(rendezVousId, userId)));
    }


    @PostMapping
    public ResponseEntity<InfoRendezVousResponse> createRendezVous(
            @RequestBody CreateRendezVousRequest createChambreRequest,
            HttpServletRequest request
    ) {
        try {
            return ResponseEntity.ok(rendezVousService.saveRendezVous(createChambreRequest));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !" + e);
        }
    }

    @PutMapping(value = "/{rendezVousId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRendezVous(
            @Valid @RequestBody UpdateRendezVousRequest updateRendezVousRequest,
            @PathVariable("rendezVousId") Long rendezVousId
    ) {
        try {
            InfoRendezVousResponse response = rendezVousService.updateRendezVous(updateRendezVousRequest, rendezVousId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            // Retourner une erreur générique
            if (e.getMessage().contains("not found") || e.getMessage().contains("n'existe pas")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rendez-vous non trouvé");
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

}
