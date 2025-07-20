package uadb.location.logement.dto.controller.chambreController.createChambre;

import uadb.location.logement.model.Chambre.Chambre;

import java.math.BigDecimal;

public record CreateChambreRequest(
        Long maisonId,
        String titre,
        String taille,
        String description,
        Chambre.Type type,
        boolean meublee,
        boolean salleDeBain,
        boolean disponible,
        BigDecimal prix) {
}
