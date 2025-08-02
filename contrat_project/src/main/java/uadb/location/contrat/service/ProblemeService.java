package uadb.location.contrat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.contrat.ContratSpecification;
import uadb.location.contrat.model.paiement.Paiement;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.model.paiement.PaiementSpecification;
import uadb.location.contrat.repositories.ContratRepository;
import uadb.location.contrat.repositories.PaiementRepository;
import uadb.location.contrat.service.Interface.IContratService;
import uadb.location.contrat.service.Interface.IPaiementService;

@Service
public class PaiementService implements IPaiementService {

    private final PaiementRepository paiementRepository;


    public PaiementService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }


    @Override
    public Page<InfoPaiementResponse> getAllPaiements(Pageable pageable, PaiementSearchCriteria paiementSearchCriteria) {
        Specification<Paiement> spec = PaiementSpecification.withCriteria(paiementSearchCriteria);
        Page<Paiement> entities = paiementRepository.findAll(spec, pageable);
        return entities.map(Paiement::toInfoPaiementResponse);
    }

}
