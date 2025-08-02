package uadb.location.contrat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.contrat.ContratSearchCriteria;
import uadb.location.contrat.model.contrat.ContratSpecification;
import uadb.location.contrat.repositories.ContratRepository;
import uadb.location.contrat.service.Interface.IContratService;

@Service
public class ContratService implements IContratService {

    private final ContratRepository contratRepository;


    public ContratService(ContratRepository contratRepository) {
        this.contratRepository = contratRepository;
    }


    @Override
    public Page<InfoContratResponse> getAllContrat(Pageable pageable, ContratSearchCriteria contratSearchCriteria) {
        Specification<Contrat> spec = ContratSpecification.withCriteria(contratSearchCriteria);
        Page<Contrat> entities = contratRepository.findAll(spec, pageable);
        return entities.map(Contrat::toInfoContratResponse);
    }
}
