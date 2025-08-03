package uadb.location.contrat.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeResponse;
import uadb.location.contrat.dto.controller.problemeController.saveProbleme.CreateProblemeRequest;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.model.probleme.ProblemeSearchCriteria;

public interface IProblemeService {
    Page<InfoProblemeResponse> getAllProblemes(Pageable pageable, ProblemeSearchCriteria problemeSearchCriteria);

    InfoProblemeResponse saveProbleme(CreateProblemeRequest createProblemeRequest);
}
