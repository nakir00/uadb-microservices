package uadb.location.contrat.dto.controller.contratController.createContrat;

import com.fasterxml.jackson.annotation.JsonFormat;
import uadb.location.contrat.model.contrat.Contrat;

import java.time.LocalDateTime;

public record CreateContratRequest(
        Long locataireId,
        Long chambreId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime dateDebut,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime dateFin,
        Double montantCaution,
        Integer moisCaution,
        String description,
        Contrat.ModePaiement modePaiement,
        Contrat.Periodicite periodicite,
        Contrat.Statut statut
) {
}