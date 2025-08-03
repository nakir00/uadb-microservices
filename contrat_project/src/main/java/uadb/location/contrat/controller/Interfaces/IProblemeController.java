package uadb.location.contrat.controller.Interfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeResponse;
import uadb.location.contrat.dto.controller.problemeController.saveProbleme.CreateProblemeRequest;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.model.probleme.ProblemeSearchCriteria;

public interface IProblemeController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoProblemeResponse>> readProblemes(
            @PageableDefault Pageable pageable,
            @ModelAttribute ProblemeSearchCriteria problemeSearchCriteria,
            HttpServletRequest request
    );

    @PostMapping
    public ResponseEntity<InfoProblemeResponse> saveProbleme(
            @RequestBody CreateProblemeRequest createProblemeRequest,
            HttpServletRequest request
    );
}
