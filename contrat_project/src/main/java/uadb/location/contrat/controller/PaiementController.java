package uadb.location.contrat.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import uadb.location.contrat.controller.Interfaces.IContratController;
import uadb.location.contrat.controller.Interfaces.IPaiementController;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.service.ContratService;
import uadb.location.contrat.service.PaiementService;

import java.util.List;

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

    @GetMapping(value = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InfoPaiementResponse>> readPaiements(
            @ModelAttribute PaiementSearchCriteria paiementSearchCriteria,
            HttpServletRequest request
    ) {

        Cookie id = WebUtils.getCookie(request, "user_id");

        Long userId = Long.parseLong(id.getValue());
        return ResponseEntity.ok(paiementService.getAllPaiementsAll(userId,paiementSearchCriteria));
    }

    @PutMapping("/{paiementId}")
    public ResponseEntity<InfoPaiementResponse> payePaiement(
            @PathVariable Long paiementId,
            HttpServletRequest request
    ) {

        Cookie id = WebUtils.getCookie(request, "user_id");
        assert id != null;
        Long userId = Long.parseLong(id.getValue());

        return ResponseEntity.ok(paiementService.payer(
                        paiementId,
                        userId
                )
        );
    }
}
