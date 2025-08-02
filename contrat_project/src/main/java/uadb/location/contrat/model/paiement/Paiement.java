package uadb.location.contrat.model.paiement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementResponse;
import uadb.location.contrat.dto.controller.paiementController.InfoPaiementWORelationsResponse;
import uadb.location.contrat.model.contrat.Contrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contrat_id", nullable = false)
    private Long contratId;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "statut")
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Column(name = "cree_le")
    private LocalDateTime creeLe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrat_id", insertable = false, updatable = false)
    @JsonBackReference
    private Contrat contrat;

    // Constructeurs
    public Paiement() {}

    public Long id() {
        return id;
    }

    public Paiement setId(Long id) {
        this.id = id;
        return this;
    }

    public Long contratId() {
        return contratId;
    }

    public Paiement setContratId(Long contratId) {
        this.contratId = contratId;
        return this;
    }

    public BigDecimal montant() {
        return montant;
    }

    public Paiement setMontant(BigDecimal montant) {
        this.montant = montant;
        return this;
    }

    public Statut statut() {
        return statut;
    }

    public Paiement setStatut(Statut statut) {
        this.statut = statut;
        return this;
    }

    public LocalDate dateEcheance() {
        return dateEcheance;
    }

    public Paiement setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
        return this;
    }

    public LocalDateTime datePaiement() {
        return datePaiement;
    }

    public Paiement setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public Paiement setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public Contrat contrat() {
        return contrat;
    }

    public Paiement setContrat(Contrat contrat) {
        this.contrat = contrat;
        return this;
    }



    public enum Statut {
        PAYE,
        IMPAYE
    }

    public static InfoPaiementResponse toInfoPaiementResponse(Paiement paiement) {
        return new InfoPaiementResponse(
                paiement.id(),
                paiement.contratId(),
                paiement.montant(),
                paiement.statut(),
                paiement.dateEcheance(),
                paiement.datePaiement(),
                paiement.creeLe(),
                Contrat.toInfoContratResponse(paiement.contrat())
        );
    }

    public static InfoPaiementWORelationsResponse toInfoPaiementWORelationsResponse(Paiement paiement) {
        return new InfoPaiementWORelationsResponse(
                paiement.id(),
                paiement.contratId(),
                paiement.montant(),
                paiement.statut(),
                paiement.dateEcheance(),
                paiement.datePaiement(),
                paiement.creeLe()
        );
    }
}
