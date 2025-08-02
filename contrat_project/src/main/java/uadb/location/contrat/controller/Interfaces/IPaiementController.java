package uadb.location.contrat.controller.Interfaces;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;

public interface IContratController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<InfoContratResponse>> readChambres(
            @PageableDefault Pageable pageable,
            @ModelAttribute ContratSearchCriteria contratSearchCriteria,
            HttpServletRequest request
    );
}
