package uadb.location.logement.dto.controller.rendezVousController.createRendezVousRequest;

import java.time.LocalDateTime;

public record CreateRendezVousRequest(
         Long locataireId,
         Long chambreId,
         LocalDateTime dateHeure
) {
}
