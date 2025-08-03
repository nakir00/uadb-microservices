package uadb.location.contrat.model.paiement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PaiementSearchCriteria {

    private Integer id;
    private Integer contratId;
    private List<Integer> contratIds;
    private BigDecimal montant;
    private BigDecimal montantMin;
    private BigDecimal montantMax;
    private Paiement.Statut statut;
    private List<String> statuts;
    private LocalDate dateEcheance;
    private LocalDate dateEcheanceFrom;
    private LocalDate dateEcheanceTo;
    private LocalDateTime datePaiement;
    private LocalDateTime datePaiementFrom;
    private LocalDateTime datePaiementTo;
    private LocalDateTime creeLe;
    private LocalDateTime creeLeFrom;
    private LocalDateTime creeLeTo;

    public List<Long> chambreIds() {
        return chambreIds;
    }

    public PaiementSearchCriteria setChambreIds(List<Long> chambreIds) {
        this.chambreIds = chambreIds;
        return this;
    }

    public PaiementSearchCriteria setPaye(Boolean paye) {
        isPaye = paye;
        return this;
    }

    public PaiementSearchCriteria setEnRetard(Boolean enRetard) {
        isEnRetard = enRetard;
        return this;
    }

    private List<Long> chambreIds;

    // Critères additionnels pour des recherches avancées
    private Boolean isPaye; // true pour PAYE, false pour IMPAYE
    private Boolean isEnRetard; // si dateEcheance < aujourd'hui et statut = IMPAYE
    private LocalDate echeanceAvant; // paiements avec échéance avant cette date
    private LocalDate echeanceApres; // paiements avec échéance après cette date

    // Constructeurs
    public PaiementSearchCriteria() {}

    public Integer id() {
        return id;
    }

    public PaiementSearchCriteria setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer contratId() {
        return contratId;
    }

    public PaiementSearchCriteria setContratId(Integer contratId) {
        this.contratId = contratId;
        return this;
    }

    public List<Integer> contratIds() {
        return contratIds;
    }

    public PaiementSearchCriteria setContratIds(List<Integer> contratIds) {
        this.contratIds = contratIds;
        return this;
    }

    public BigDecimal montant() {
        return montant;
    }

    public PaiementSearchCriteria setMontant(BigDecimal montant) {
        this.montant = montant;
        return this;
    }

    public BigDecimal montantMin() {
        return montantMin;
    }

    public PaiementSearchCriteria setMontantMin(BigDecimal montantMin) {
        this.montantMin = montantMin;
        return this;
    }

    public BigDecimal montantMax() {
        return montantMax;
    }

    public PaiementSearchCriteria setMontantMax(BigDecimal montantMax) {
        this.montantMax = montantMax;
        return this;
    }

    public Paiement.Statut statut() {
        return statut;
    }

    public PaiementSearchCriteria setStatut(Paiement.Statut statut) {
        this.statut = statut;
        return this;
    }

    public List<String> statuts() {
        return statuts;
    }

    public PaiementSearchCriteria setStatuts(List<String> statuts) {
        this.statuts = statuts;
        return this;
    }

    public LocalDate dateEcheance() {
        return dateEcheance;
    }

    public PaiementSearchCriteria setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
        return this;
    }

    public LocalDate dateEcheanceFrom() {
        return dateEcheanceFrom;
    }

    public PaiementSearchCriteria setDateEcheanceFrom(LocalDate dateEcheanceFrom) {
        this.dateEcheanceFrom = dateEcheanceFrom;
        return this;
    }

    public LocalDate dateEcheanceTo() {
        return dateEcheanceTo;
    }

    public PaiementSearchCriteria setDateEcheanceTo(LocalDate dateEcheanceTo) {
        this.dateEcheanceTo = dateEcheanceTo;
        return this;
    }

    public LocalDateTime datePaiement() {
        return datePaiement;
    }

    public PaiementSearchCriteria setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
        return this;
    }

    public LocalDateTime datePaiementFrom() {
        return datePaiementFrom;
    }

    public PaiementSearchCriteria setDatePaiementFrom(LocalDateTime datePaiementFrom) {
        this.datePaiementFrom = datePaiementFrom;
        return this;
    }

    public LocalDateTime datePaiementTo() {
        return datePaiementTo;
    }

    public PaiementSearchCriteria setDatePaiementTo(LocalDateTime datePaiementTo) {
        this.datePaiementTo = datePaiementTo;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public PaiementSearchCriteria setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public LocalDateTime creeLeFrom() {
        return creeLeFrom;
    }

    public PaiementSearchCriteria setCreeLeFrom(LocalDateTime creeLeFrom) {
        this.creeLeFrom = creeLeFrom;
        return this;
    }

    public LocalDateTime creeLeTo() {
        return creeLeTo;
    }

    public PaiementSearchCriteria setCreeLeTo(LocalDateTime creeLeTo) {
        this.creeLeTo = creeLeTo;
        return this;
    }

    public Boolean isPaye() {
        return isPaye;
    }

    public PaiementSearchCriteria setIsPaye(Boolean isPaye) {
        this.isPaye = isPaye;
        return this;
    }

    public Boolean isEnRetard() {
        return isEnRetard;
    }

    public PaiementSearchCriteria setIsEnRetard(Boolean isEnRetard) {
        this.isEnRetard = isEnRetard;
        return this;
    }

    public LocalDate echeanceAvant() {
        return echeanceAvant;
    }

    public PaiementSearchCriteria setEcheanceAvant(LocalDate echeanceAvant) {
        this.echeanceAvant = echeanceAvant;
        return this;
    }

    public LocalDate echeanceApres() {
        return echeanceApres;
    }

    public PaiementSearchCriteria setEcheanceApres(LocalDate echeanceApres) {
        this.echeanceApres = echeanceApres;
        return this;
    }

    // Méthodes utilitaires pour faciliter l'utilisation

    /**
     * Configure les critères pour rechercher les paiements payés
     */
    public PaiementSearchCriteria payes() {
        this.isPaye = true;
        this.statut = Paiement.Statut.PAYE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les paiements impayés
     */
    public PaiementSearchCriteria impayes() {
        this.isPaye = false;
        this.statut = Paiement.Statut.IMPAYE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les paiements en retard
     */
    public PaiementSearchCriteria enRetard() {
        this.isEnRetard = true;
        this.statut = Paiement.Statut.IMPAYE;
        this.echeanceAvant = LocalDate.now();
        return this;
    }

    /**
     * Configure les critères pour rechercher les paiements à venir
     */
    public PaiementSearchCriteria aVenir() {
        this.statut = Paiement.Statut.IMPAYE;
        this.echeanceApres = LocalDate.now();
        return this;
    }

    /**
     * Configure les critères pour une plage de montants
     */
    public PaiementSearchCriteria entreMontants(BigDecimal min, BigDecimal max) {
        this.montantMin = min;
        this.montantMax = max;
        return this;
    }

    /**
     * Configure les critères pour une plage de dates d'échéance
     */
    public PaiementSearchCriteria entreEcheances(LocalDate from, LocalDate to) {
        this.dateEcheanceFrom = from;
        this.dateEcheanceTo = to;
        return this;
    }

    /**
     * Configure les critères pour une plage de dates de paiement
     */
    public PaiementSearchCriteria entrePaiements(LocalDateTime from, LocalDateTime to) {
        this.datePaiementFrom = from;
        this.datePaiementTo = to;
        return this;
    }
}
