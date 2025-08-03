package uadb.location.logement.services.Interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uadb.location.logement.dto.controller.rendezVousController.InfoRendezVousResponse;
import uadb.location.logement.dto.controller.rendezVousController.UpdateRendezVousRequest.UpdateRendezVousRequest;
import uadb.location.logement.dto.controller.rendezVousController.createRendezVousRequest.CreateRendezVousRequest;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.rendezVous.RendezVous;
import uadb.location.logement.model.rendezVous.RendezVousSearchCriteria;

public interface IRendezVousService {
    Page<InfoRendezVousResponse> getAllRendezVous(Pageable pageable, RendezVousSearchCriteria rendezVousSearchCriteria);

    InfoRendezVousResponse saveRendezVous(CreateRendezVousRequest createChambreRequest);


    InfoRendezVousResponse updateRendezVous(UpdateRendezVousRequest updateRendezVousRequest, Long rendezVousId);

    RendezVous getRendezVousById(Long rendezVousId, Long userId);
}
