package uadb.location.contrat.dto.controller.contratController;

import uadb.location.contrat.dto.client.ChambreClient.ReadChambreDTO;
import uadb.location.contrat.dto.client.UtilisateurClient.ReadUtilisateurDTO;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementWORelationsResponse;
import uadb.location.contrat.dto.controller.problemeController.InfoProblemeWORelationsResponse;
import uadb.location.contrat.model.contrat.Contrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InfoContratWChambreAndLocataireResponse(
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
        LocalDateTime creeLe,
        ReadUtilisateurDTO locataire,
        ReadChambreDTO chambre,
        List<InfoPaiementWORelationsResponse> paiements, //  a remplacer
        List<InfoProblemeWORelationsResponse> problemes  // a remplacer
) {
}
