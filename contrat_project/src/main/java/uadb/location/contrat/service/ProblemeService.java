package uadb.location.contrat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeResponse;
import uadb.location.contrat.dto.controller.problemeController.saveProbleme.CreateProblemeRequest;
import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.paiement.Paiement;
import uadb.location.contrat.model.paiement.PaiementSearchCriteria;
import uadb.location.contrat.model.paiement.PaiementSpecification;
import uadb.location.contrat.model.probleme.Probleme;
import uadb.location.contrat.model.probleme.ProblemeSearchCriteria;
import uadb.location.contrat.model.probleme.ProblemeSpecification;
import uadb.location.contrat.repositories.ContratRepository;
import uadb.location.contrat.repositories.PaiementRepository;
import uadb.location.contrat.repositories.ProblemeRepository;
import uadb.location.contrat.service.Interface.IProblemeService;

import java.util.Optional;

@Service
public class ProblemeService implements IProblemeService {

    private final ProblemeRepository problemeRepository;
    private  final ContratRepository contratRepository;


    public ProblemeService(ProblemeRepository problemeRepository, ContratRepository contratRepository) {
        this.problemeRepository = problemeRepository;
        this.contratRepository = contratRepository;
    }

    @Override
    public Page<InfoProblemeResponse> getAllProblemes(Pageable pageable, ProblemeSearchCriteria problemeSearchCriteria) {
        Specification<Probleme> spec = ProblemeSpecification.withCriteria(problemeSearchCriteria);
        Page<Probleme> entities = problemeRepository.findAll(spec, pageable);
        return entities.map(Probleme::toInfoProblemeResponse);
    }

    @Override
    public InfoProblemeResponse saveProbleme(CreateProblemeRequest createProblemeRequest) {

        Probleme probleme = Probleme.fromCreateProblemeRequest(createProblemeRequest);
        Optional<Contrat> contrat = contratRepository.findById(createProblemeRequest.contratId());
        if (contrat.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contrat non retrouve");
        }
        probleme.setContrat(contrat.get());
        Probleme problemeSaved = problemeRepository.save(probleme);
        return Probleme.toInfoProblemeResponse(problemeSaved);
    }

    public InfoProblemeResponse validateProbleme(Long problemeId) {

        Optional<Probleme> problemeOptional = problemeRepository.findById(problemeId);
        if (problemeOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contrat non retrouve");
        }
        Probleme probleme = problemeOptional.get();
        probleme.setResolu(true);
        Probleme problemeSaved = problemeRepository.save(probleme);
        return Probleme.toInfoProblemeResponse(problemeSaved);
    }
}
