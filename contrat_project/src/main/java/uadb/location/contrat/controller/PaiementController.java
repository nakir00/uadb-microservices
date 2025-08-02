package uadb.location.contrat.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import uadb.location.contrat.controller.Interfaces.IContratController;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
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
    public ResponseEntity<Page<InfoContratResponse>> readChambres(
            @PageableDefault Pageable pageable,
            @ModelAttribute ContratSearchCriteria contratSearchCriteria,
            HttpServletRequest request
    ) {

        /*Cookie id = WebUtils.getCookie(request, "user_id");

        if (id != null) {
            Long userId = Long.parseLong(id.getValue());
            contratSearchCriteria.setCha(userId);
        }*/
        return ResponseEntity.ok(contratService.getAllContrat(pageable, contratSearchCriteria));
    }
}
