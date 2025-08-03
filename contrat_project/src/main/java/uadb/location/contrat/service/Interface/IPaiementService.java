package uadb.location.contrat.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;

import java.util.List;

public interface IPaiementService {
    Page<InfoPaiementResponse> getAllPaiements(Pageable pageable, PaiementSearchCriteria paiementSearchCriteria);

    InfoPaiementResponse payer(Long paiementId, Long userId);

    public List<InfoPaiementResponse> getAllPaiementsAll(Long userId, PaiementSearchCriteria paiementSearchCriteria);
}
