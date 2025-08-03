package uadb.location.contrat.model.contrat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ContratSearchCriteria {

    private Long id;
    private Long locataireId;
    private Long chambreId;
    private List<Long> chambreIds;
    private LocalDate dateDebut;
    private LocalDate dateDebutFrom;
    private LocalDate dateDebutTo;
    private LocalDate dateFin;
    private LocalDate dateFinFrom;
    private LocalDate dateFinTo;
    private BigDecimal montantCaution;
    private BigDecimal montantCautionMin;
    private BigDecimal montantCautionMax;
    private Integer moisCaution;
    private Integer moisCautionMin;
    private Integer moisCautionMax;
    private String description;
    private String modePaiement;
    private List<String> modesPaiement;
    private String periodicite;
    private List<String> periodicites;
    private String statut;
    private List<String> statuts;
    private LocalDateTime creeLe;
    private LocalDateTime creeLeFrom;
    private LocalDateTime creeLeTo;

    // Constructeurs
    public ContratSearchCriteria() {}


    public Long id() {
        return id;
    }

    public ContratSearchCriteria setId(Long id) {
        this.id = id;
        return this;
    }

    public Long locataireId() {
        return locataireId;
    }

    public ContratSearchCriteria setLocataireId(Long locataireId) {
        this.locataireId = locataireId;
        return this;
    }

    public Long chambreId() {
        return chambreId;
    }

    public ContratSearchCriteria setChambreId(Long chambreId) {
        this.chambreId = chambreId;
        return this;
    }

    public List<Long> chambreIds() {
        return chambreIds;
    }

    public ContratSearchCriteria setChambreIds(List<Long> chambreIds) {
        this.chambreIds = chambreIds;
        return this;
    }

    public LocalDate dateDebut() {
        return dateDebut;
    }

    public ContratSearchCriteria setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public LocalDate dateDebutFrom() {
        return dateDebutFrom;
    }

    public ContratSearchCriteria setDateDebutFrom(LocalDate dateDebutFrom) {
        this.dateDebutFrom = dateDebutFrom;
        return this;
    }

    public LocalDate dateDebutTo() {
        return dateDebutTo;
    }

    public ContratSearchCriteria setDateDebutTo(LocalDate dateDebutTo) {
        this.dateDebutTo = dateDebutTo;
        return this;
    }

    public LocalDate dateFin() {
        return dateFin;
    }

    public ContratSearchCriteria setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public LocalDate dateFinFrom() {
        return dateFinFrom;
    }

    public ContratSearchCriteria setDateFinFrom(LocalDate dateFinFrom) {
        this.dateFinFrom = dateFinFrom;
        return this;
    }

    public LocalDate dateFinTo() {
        return dateFinTo;
    }

    public ContratSearchCriteria setDateFinTo(LocalDate dateFinTo) {
        this.dateFinTo = dateFinTo;
        return this;
    }

    public BigDecimal montantCaution() {
        return montantCaution;
    }

    public ContratSearchCriteria setMontantCaution(BigDecimal montantCaution) {
        this.montantCaution = montantCaution;
        return this;
    }

    public BigDecimal montantCautionMin() {
        return montantCautionMin;
    }

    public ContratSearchCriteria setMontantCautionMin(BigDecimal montantCautionMin) {
        this.montantCautionMin = montantCautionMin;
        return this;
    }

    public BigDecimal montantCautionMax() {
        return montantCautionMax;
    }

    public ContratSearchCriteria setMontantCautionMax(BigDecimal montantCautionMax) {
        this.montantCautionMax = montantCautionMax;
        return this;
    }

    public Integer moisCaution() {
        return moisCaution;
    }

    public ContratSearchCriteria setMoisCaution(Integer moisCaution) {
        this.moisCaution = moisCaution;
        return this;
    }

    public Integer moisCautionMin() {
        return moisCautionMin;
    }

    public ContratSearchCriteria setMoisCautionMin(Integer moisCautionMin) {
        this.moisCautionMin = moisCautionMin;
        return this;
    }

    public Integer moisCautionMax() {
        return moisCautionMax;
    }

    public ContratSearchCriteria setMoisCautionMax(Integer moisCautionMax) {
        this.moisCautionMax = moisCautionMax;
        return this;
    }

    public String description() {
        return description;
    }

    public ContratSearchCriteria setDescription(String description) {
        this.description = description;
        return this;
    }

    public String modePaiement() {
        return modePaiement;
    }

    public ContratSearchCriteria setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
        return this;
    }

    public List<String> modesPaiement() {
        return modesPaiement;
    }

    public ContratSearchCriteria setModesPaiement(List<String> modesPaiement) {
        this.modesPaiement = modesPaiement;
        return this;
    }

    public String periodicite() {
        return periodicite;
    }

    public ContratSearchCriteria setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
        return this;
    }

    public List<String> periodicites() {
        return periodicites;
    }

    public ContratSearchCriteria setPeriodicites(List<String> periodicites) {
        this.periodicites = periodicites;
        return this;
    }

    public String statut() {
        return statut;
    }

    public ContratSearchCriteria setStatut(String statut) {
        this.statut = statut;
        return this;
    }

    public List<String> statuts() {
        return statuts;
    }

    public ContratSearchCriteria setStatuts(List<String> statuts) {
        this.statuts = statuts;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public ContratSearchCriteria setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public LocalDateTime creeLeFrom() {
        return creeLeFrom;
    }

    public ContratSearchCriteria setCreeLeFrom(LocalDateTime creeLeFrom) {
        this.creeLeFrom = creeLeFrom;
        return this;
    }

    public LocalDateTime creeLeTo() {
        return creeLeTo;
    }

    public ContratSearchCriteria setCreeLeTo(LocalDateTime creeLeTo) {
        this.creeLeTo = creeLeTo;
        return this;
    }
}
