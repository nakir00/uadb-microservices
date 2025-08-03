package uadb.location.contrat.dto.controller.contratController;

import uadb.location.contrat.model.contrat.Contrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InfoContratWORelationsResponse(
        Long id,
        Long locataireId,
        Long chambreId,
        LocalDate dateDebut,
        LocalDate dateFin,
        BigDecimal montantCaution,
        Integer moisCaution,
        String description,
        Contrat.ModePaiement modePaiement,
        Contrat.Periodicite periodicite,
        Contrat.Statut statut,
        LocalDateTime creeLe
) {
}
