package uadb.location.contrat.dto.controller.paiementController;

import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.paiement.Paiement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InfoPaiementWORelationsResponse(
        Long id,
        Long contratId,
        BigDecimal montant,
        Paiement.Statut statut,
        LocalDate dateEcheance,
        LocalDateTime datePaiement,
        LocalDateTime creeLe
) {
}
