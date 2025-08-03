package uadb.location.logement.services.Interfaces;

import org.springframework.data.domain.Page;
import uadb.location.logement.dto.controller.chambreController.InfoChambreResponse;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.Chambre.ChambreSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface IChambreService {

    void deleteChambre(Long proprietaireId, Long maisonId);

    Chambre getChambreById(Long userId, Long chambreId);

    List<InfoChambreResponse> getAllChambresWOPage(ChambreSearchCriteria chambreSearchCriteria);
}
