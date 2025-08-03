package uadb.location.contrat.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.contrat.controller.Interfaces.IPaiementController;
import uadb.location.contrat.controller.Interfaces.IProblemeController;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.contratController.createContrat.CreateContratRequest;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeResponse;
import uadb.location.contrat.dto.controller.problemeController.saveProbleme.CreateProblemeRequest;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.model.probleme.ProblemeSearchCriteria;
import uadb.location.contrat.service.PaiementService;
import uadb.location.contrat.service.ProblemeService;

@RestController
@RequestMapping("api/probleme")
public class ProblemeController implements IProblemeController {

    private final ProblemeService problemeService;

    public ProblemeController(ProblemeService problemeService) {
        this.problemeService = problemeService;
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoProblemeResponse>> readProblemes(
            @PageableDefault Pageable pageable,
            @ModelAttribute ProblemeSearchCriteria problemeSearchCriteria,
            HttpServletRequest request) {
        return ResponseEntity.ok(problemeService.getAllProblemes(pageable, problemeSearchCriteria));
    }

    @PostMapping
    public ResponseEntity<InfoProblemeResponse> saveProbleme(
            @RequestBody CreateProblemeRequest createProblemeRequest,
            HttpServletRequest request
    ) {
        try {
            return ResponseEntity.ok(problemeService.saveProbleme(createProblemeRequest));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !" + e);
        }
    }

    @PutMapping(value = "/{problemeId}/resolu")
    public ResponseEntity<InfoProblemeResponse> validerProbleme(
            @PathVariable Long problemeId
    ) {
        try {
            return ResponseEntity.ok(problemeService.validateProbleme(problemeId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "information manquante dans la requete !" + e);
        }
    }
}
