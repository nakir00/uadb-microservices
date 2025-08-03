package uadb.location.contrat.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import uadb.location.contrat.controller.Interfaces.IContratController;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.contratController.createContrat.CreateContratRequest;
import uadb.location.contrat.dto.controller.contratController.updateContrat.UpdateContratRequest;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeResponse;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.service.ContratService;

@RestController
@RequestMapping("api/contrat")
public class ContratController implements IContratController {

    private final ContratService contratService;

    public ContratController(ContratService contratService) {
        this.contratService = contratService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoContratResponse>> readContrats(
            @PageableDefault Pageable pageable,
            @ModelAttribute ContratSearchCriteria contratSearchCriteria,
            HttpServletRequest request
    ) {

        Cookie id = WebUtils.getCookie(request, "user_id");
        Long userId = Long.parseLong(id.getValue());

        return ResponseEntity.ok(contratService.getAllContrat(pageable, contratSearchCriteria, userId));
    }

    @PostMapping
    public ResponseEntity<InfoContratResponse> createContrat(
            @RequestBody CreateContratRequest createContratRequest,
            HttpServletRequest request
    ) {
        System.out.println(createContratRequest);
        try {
            /*Cookie id = WebUtils.getCookie(request, "user_id");
            assert id != null;
            Long userId = Long.parseLong(id.getValue());*/
            System.out.println(createContratRequest);

            return ResponseEntity.ok(contratService.saveContrat(createContratRequest));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !" + e);
        }
    }

    @GetMapping(value = "/{contratId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> readContrat(
            @PathVariable Long contratId,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(contratService.getContratById(contratId));
    }

    @PutMapping(value = "/{contratId}")
    public ResponseEntity<InfoContratResponse> updateContrat(
            @PathVariable Long contratId,
            @RequestBody UpdateContratRequest updateContratRequest
    ) {
        try {
            return ResponseEntity.ok(contratService.updateContrat(contratId, updateContratRequest));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "information manquante dans la requete !" + e);
        }
    }
}
