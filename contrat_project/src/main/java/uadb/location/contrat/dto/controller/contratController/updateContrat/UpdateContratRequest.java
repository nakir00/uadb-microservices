package uadb.location.contrat.dto.controller.contratController.updateContrat;

import com.fasterxml.jackson.annotation.JsonFormat;
import uadb.location.contrat.model.contrat.Contrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdateContratRequest(
        Long locataireId,
        Long chambreId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDate dateDebut,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDate dateFin,
        BigDecimal montantCaution,
        Integer moisCaution,
        String description,
        Contrat.ModePaiement modePaiement,
        Contrat.Periodicite periodicite,
        Contrat.Statut statut
) {
}