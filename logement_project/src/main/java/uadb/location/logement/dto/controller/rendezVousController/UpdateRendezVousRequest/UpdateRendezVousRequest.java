package uadb.location.logement.dto.controller.rendezVousController.UpdateRendezVousRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import uadb.location.logement.model.rendezVous.RendezVous;

import java.time.LocalDateTime;

public record UpdateRendezVousRequest(

        Long locataireId,

        // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime dateHeure,

        RendezVous.Statut statut,

        Long chambreId
) {
}
