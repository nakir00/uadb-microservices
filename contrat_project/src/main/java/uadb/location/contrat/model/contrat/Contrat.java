package uadb.location.contrat.model.contrat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.contrat.dto.client.ChambreClient.ReadChambreDTO;
import uadb.location.contrat.dto.client.UtilisateurClient.ReadUtilisateurDTO;
import uadb.location.contrat.dto.controller.contratController.InfoContratResponse;
import uadb.location.contrat.dto.controller.contratController.InfoContratWChambreAndLocataireResponse;
import uadb.location.contrat.dto.controller.contratController.InfoContratWORelationsResponse;
import uadb.location.contrat.dto.controller.contratController.updateContrat.UpdateContratRequest;
import uadb.location.contrat.model.paiement.Paiement;
import uadb.location.contrat.model.probleme.Probleme;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contrats")
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "locataire_id", nullable = false)
    private Long locataireId;

    @Column(name = "chambre_id", nullable = false)
    private Long chambreId;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "montant_caution", nullable = false)
    private BigDecimal montantCaution;

    @Column(name = "mois_caution", nullable = false)
    private Integer moisCaution;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "mode_paiement", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    @Column(name = "periodicite", nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodicite periodicite;

    @Column(name = "statut", nullable = false)
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(name = "cree_le", nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creeLe;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Probleme> problemes = new ArrayList<>();

    // Constructeurs
    public Contrat() {
    }

    public Long id() {
        return id;
    }

    public Contrat setId(Long id) {
        this.id = id;
        return this;
    }

    public Long locataireId() {
        return locataireId;
    }

    public Contrat setLocataireId(Long locataireId) {
        this.locataireId = locataireId;
        return this;
    }

    public Long chambreId() {
        return chambreId;
    }

    public Contrat setChambreId(Long chambreId) {
        this.chambreId = chambreId;
        return this;
    }

    public LocalDate dateDebut() {
        return dateDebut;
    }

    public Contrat setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate dateFin() {
        return dateFin;
    }

    public Contrat setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public BigDecimal montantCaution() {
        return montantCaution;
    }

    public Contrat setMontantCaution(BigDecimal montantCaution) {
        this.montantCaution = montantCaution;
        return this;
    }

    public Integer moisCaution() {
        return moisCaution;
    }

    public Contrat setMoisCaution(Integer moisCaution) {
        this.moisCaution = moisCaution;
        return this;
    }

    public String description() {
        return description;
    }

    public Contrat setDescription(String description) {
        this.description = description;
        return this;
    }

    public ModePaiement modePaiement() {
        return modePaiement;
    }

    public Contrat setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
        return this;
    }

    public Periodicite periodicite() {
        return periodicite;
    }

    public Contrat setPeriodicite(Periodicite periodicite) {
        this.periodicite = periodicite;
        return this;
    }

    public Statut statut() {
        return statut;
    }

    public Contrat setStatut(Statut statut) {
        this.statut = statut;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public Contrat setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public List<Paiement> paiements() {
        return paiements;
    }

    public Contrat setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
        return this;
    }

    public List<Probleme> problemes() {
        return problemes;
    }

    public Contrat setProblemes(List<Probleme> problemes) {
        this.problemes = problemes;
        return this;
    }

    public enum ModePaiement {
        VIREMENT,
        CASH,
        MOBILEMONEY
    }

    public enum Periodicite {
        JOURNALIER,
        HEBDOMADAIRE,
        MENSUEL
    }
    public enum Statut {
        ACTIF,
        RESILIE
    }

    public static InfoContratResponse toInfoContratResponse(Contrat contrat) {
        return  new InfoContratResponse(
                contrat.id(),
                contrat.locataireId(),
                contrat.chambreId(),
                contrat.dateDebut(),
                contrat.dateFin(),
                contrat.montantCaution(),
                contrat.moisCaution(),
                contrat.description(),
                contrat.modePaiement(),
                contrat.periodicite(),
                contrat.statut(),
                contrat.creeLe(),
                contrat.paiements().stream().map(Paiement::toInfoPaiementWORelationsResponse).toList(),
                contrat.problemes().stream().map(Probleme::toInfoProblemeWORelationsResponse).toList()
        );
    }

    public static InfoContratWORelationsResponse toInfoContratWORelationsResponse(Contrat contrat) {
        return  new InfoContratWORelationsResponse(
                contrat.id(),
                contrat.locataireId(),
                contrat.chambreId(),
                contrat.dateDebut(),
                contrat.dateFin(),
                contrat.montantCaution(),
                contrat.moisCaution(),
                contrat.description(),
                contrat.modePaiement(),
                contrat.periodicite(),
                contrat.statut(),
                contrat.creeLe()
        );
    }
    public static InfoContratWChambreAndLocataireResponse toInfoContratWChambreAndLocataireResponse(Contrat contrat, ReadChambreDTO chambre, ReadUtilisateurDTO utilisateur) {
        return  new InfoContratWChambreAndLocataireResponse(
                contrat.id(),
                contrat.locataireId(),
                contrat.chambreId(),
                contrat.dateDebut(),
                contrat.dateFin(),
                contrat.montantCaution(),
                contrat.moisCaution(),
                contrat.description(),
                contrat.modePaiement(),
                contrat.periodicite(),
                contrat.statut(),
                contrat.creeLe(),
                utilisateur,
                chambre,
                contrat.paiements().stream().map(Paiement::toInfoPaiementWORelationsResponse).toList(),
                contrat.problemes().stream().map(Probleme::toInfoProblemeWORelationsResponse).toList()
        );
    }
    public static Contrat fromUpdateContratRequest(UpdateContratRequest contrat) {
        return  new Contrat()
                .setLocataireId(contrat.locataireId())
                .setChambreId(contrat.chambreId())
                .setDateDebut(contrat.dateDebut())
                .setDateFin(contrat.dateFin())
                .setMoisCaution(contrat.moisCaution())
                .setMontantCaution(contrat.montantCaution())
                .setDescription(contrat.description())
                .setModePaiement(contrat.modePaiement())
                .setPeriodicite(contrat.periodicite())
                .setStatut(contrat.statut());
    }

}
/*

public record UpdateContratRequest(
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
}*/
