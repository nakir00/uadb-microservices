package uadb.location.logement.services.Interfaces;

import uadb.location.logement.dto.controller.maisonController.createMaison.CreateMaisonRequest;
import uadb.location.logement.model.maison.Maison;

public interface IMaisonService {

    public Maison CreateMaison(CreateMaisonRequest infoMaisonRequest, Long proprietaireId);
}
