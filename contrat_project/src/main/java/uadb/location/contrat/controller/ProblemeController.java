package uadb.location.contrat.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uadb.location.contrat.controller.Interfaces.IContratController;
import uadb.location.contrat.controller.Interfaces.IPaiementController;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.service.ContratService;
import uadb.location.contrat.service.PaiementService;

@RestController
@RequestMapping("api/paiement")
public class PaiementController implements IPaiementController {

    private final PaiementService paiementService;

    public PaiementController(PaiementService paiementService) {
        this.paiementService = paiementService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<InfoPaiementResponse>> readPaiements(
            @PageableDefault Pageable pageable,
            @ModelAttribute PaiementSearchCriteria paiementSearchCriteria,
            HttpServletRequest request
    ) {

        /*Cookie id = WebUtils.getCookie(request, "user_id");

        if (id != null) {
            Long userId = Long.parseLong(id.getValue());
            contratSearchCriteria.setCha(userId);
        }*/
        return ResponseEntity.ok(paiementService.getAllPaiements(pageable, paiementSearchCriteria));
    }
}
