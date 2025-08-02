package uadb.location.contrat.dto.controller.contratController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.contrat.model.contrat.Contrat;
import uadb.location.contrat.model.paiement.Paiement;
import uadb.location.contrat.model.probleme.Probleme;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record InfoContratResponse(
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
        List<Paiement> paiements, //  a remplacer
        List<Probleme> problemes  // a remplacer
) {
}
