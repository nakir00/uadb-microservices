package uadb.location.contrat.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.contratController.InfoContratWChambreAndLocataireResponse;
import uadb.location.contrat.dto.controller.contratController.createContrat.CreateContratRequest;
import uadb.location.contrat.dto.controller.contratController.updateContrat.UpdateContratRequest;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;

public interface IContratService {
    Page<InfoContratResponse> getAllContrat(Pageable pageable, ContratSearchCriteria contratSearchCriteria, Long userId);

    InfoContratResponse saveContrat(CreateContratRequest createContratRequest);

    InfoContratWChambreAndLocataireResponse getContratById(Long contratId);

    InfoContratResponse updateContrat(Long contratId, UpdateContratRequest updateContratRequest);
}
