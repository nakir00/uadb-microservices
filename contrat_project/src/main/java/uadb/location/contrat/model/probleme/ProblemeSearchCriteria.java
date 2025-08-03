package uadb.location.contrat.model.probleme;

import java.time.LocalDateTime;
import java.util.List;

public class ProblemeSearchCriteria {

    private Integer id;
    private Integer contratId;
    private List<Integer> contratIds;
    private Integer signalePar;
    private List<Integer> signaleParList;
    private String description;
    private Probleme.Type type;
    private List<Probleme.Type> types;
    private Probleme.Responsable responsable;
    private List<Probleme.Responsable> responsables;
    private Boolean resolu;
    private LocalDateTime creeLe;
    private LocalDateTime creeLeFrom;
    private LocalDateTime creeLeTo;

    // Critères additionnels pour des recherches avancées
    private Boolean isEnCours; // !resolu
    private Boolean isResolu; // resolu == true
    private Integer nombreJoursDepuisCreation; // problèmes créés il y a X jours
    private Integer nombreJoursDepuisCreationMin; // au moins X jours
    private Integer nombreJoursDepuisCreationMax; // au plus X jours
    private LocalDateTime creeAvant; // créé avant cette date
    private LocalDateTime creeApres; // créé après cette date

    // Constructeurs
    public ProblemeSearchCriteria() {}

    public Integer id() {
        return id;
    }

    public ProblemeSearchCriteria setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer contratId() {
        return contratId;
    }

    public ProblemeSearchCriteria setContratId(Integer contratId) {
        this.contratId = contratId;
        return this;
    }

    public List<Integer> contratIds() {
        return contratIds;
    }

    public ProblemeSearchCriteria setContratIds(List<Integer> contratIds) {
        this.contratIds = contratIds;
        return this;
    }

    public Integer signalePar() {
        return signalePar;
    }

    public ProblemeSearchCriteria setSignalePar(Integer signalePar) {
        this.signalePar = signalePar;
        return this;
    }

    public List<Integer> signaleParList() {
        return signaleParList;
    }

    public ProblemeSearchCriteria setSignaleParList(List<Integer> signaleParList) {
        this.signaleParList = signaleParList;
        return this;
    }

    public String description() {
        return description;
    }

    public ProblemeSearchCriteria setDescription(String description) {
        this.description = description;
        return this;
    }

    public Probleme.Type type() {
        return type;
    }

    public ProblemeSearchCriteria setType(Probleme.Type type) {
        this.type = type;
        return this;
    }

    public List<Probleme.Type> types() {
        return types;
    }

    public ProblemeSearchCriteria setTypes(List<Probleme.Type> types) {
        this.types = types;
        return this;
    }

    public Probleme.Responsable responsable() {
        return responsable;
    }

    public ProblemeSearchCriteria setResponsable(Probleme.Responsable responsable) {
        this.responsable = responsable;
        return this;
    }

    public List<Probleme.Responsable> responsables() {
        return responsables;
    }

    public ProblemeSearchCriteria setResponsables(List<Probleme.Responsable> responsables) {
        this.responsables = responsables;
        return this;
    }

    public Boolean resolu() {
        return resolu;
    }

    public ProblemeSearchCriteria setResolu(Boolean resolu) {
        this.resolu = resolu;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public ProblemeSearchCriteria setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public LocalDateTime creeLeFrom() {
        return creeLeFrom;
    }

    public ProblemeSearchCriteria setCreeLeFrom(LocalDateTime creeLeFrom) {
        this.creeLeFrom = creeLeFrom;
        return this;
    }

    public LocalDateTime creeLeTo() {
        return creeLeTo;
    }

    public ProblemeSearchCriteria setCreeLeTo(LocalDateTime creeLeTo) {
        this.creeLeTo = creeLeTo;
        return this;
    }

    public Boolean isEnCours() {
        return isEnCours;
    }

    public ProblemeSearchCriteria setIsEnCours(Boolean isEnCours) {
        this.isEnCours = isEnCours;
        return this;
    }

    public Boolean isResolu() {
        return isResolu;
    }

    public ProblemeSearchCriteria setIsResolu(Boolean isResolu) {
        this.isResolu = isResolu;
        return this;
    }

    public Integer nombreJoursDepuisCreation() {
        return nombreJoursDepuisCreation;
    }

    public ProblemeSearchCriteria setNombreJoursDepuisCreation(Integer nombreJoursDepuisCreation) {
        this.nombreJoursDepuisCreation = nombreJoursDepuisCreation;
        return this;
    }

    public Integer nombreJoursDepuisCreationMin() {
        return nombreJoursDepuisCreationMin;
    }

    public ProblemeSearchCriteria setNombreJoursDepuisCreationMin(Integer nombreJoursDepuisCreationMin) {
        this.nombreJoursDepuisCreationMin = nombreJoursDepuisCreationMin;
        return this;
    }

    public Integer nombreJoursDepuisCreationMax() {
        return nombreJoursDepuisCreationMax;
    }

    public ProblemeSearchCriteria setNombreJoursDepuisCreationMax(Integer nombreJoursDepuisCreationMax) {
        this.nombreJoursDepuisCreationMax = nombreJoursDepuisCreationMax;
        return this;
    }

    public LocalDateTime creeAvant() {
        return creeAvant;
    }

    public ProblemeSearchCriteria setCreeAvant(LocalDateTime creeAvant) {
        this.creeAvant = creeAvant;
        return this;
    }

    public LocalDateTime creeApres() {
        return creeApres;
    }

    public ProblemeSearchCriteria setCreeApres(LocalDateTime creeApres) {
        this.creeApres = creeApres;
        return this;
    }

    // Méthodes utilitaires pour faciliter l'utilisation

    /**
     * Configure les critères pour rechercher les problèmes résolus
     */
    public ProblemeSearchCriteria resolus() {
        this.resolu = true;
        this.isResolu = true;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes en cours (non résolus)
     */
    public ProblemeSearchCriteria enCours() {
        this.resolu = false;
        this.isEnCours = true;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes de plomberie
     */
    public ProblemeSearchCriteria plomberie() {
        this.type = Probleme.Type.PLOMBERIE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes d'électricité
     */
    public ProblemeSearchCriteria electricite() {
        this.type = Probleme.Type.ELECTRICITE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les autres problèmes
     */
    public ProblemeSearchCriteria autres() {
        this.type = Probleme.Type.AUTRE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes sous la responsabilité du locataire
     */
    public ProblemeSearchCriteria responsabiliteLocataire() {
        this.responsable = Probleme.Responsable.LOCATAIRE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes sous la responsabilité du propriétaire
     */
    public ProblemeSearchCriteria responsabiliteProprietaire() {
        this.responsable = Probleme.Responsable.PROPRIETAIRE;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes signalés par une personne spécifique
     */
    public ProblemeSearchCriteria signalesPar(Integer personneId) {
        this.signalePar = personneId;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes anciens (plus de X jours)
     */
    public ProblemeSearchCriteria anciens(Integer nombreJours) {
        this.nombreJoursDepuisCreationMin = nombreJours;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes récents (moins de X jours)
     */
    public ProblemeSearchCriteria recents(Integer nombreJours) {
        this.nombreJoursDepuisCreationMax = nombreJours;
        return this;
    }

    /**
     * Configure les critères pour une plage de dates de création
     */
    public ProblemeSearchCriteria entreCreations(LocalDateTime from, LocalDateTime to) {
        this.creeLeFrom = from;
        this.creeLeTo = to;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes créés aujourd'hui
     */
    public ProblemeSearchCriteria creesAujourdhui() {
        LocalDateTime debutJour = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finJour = debutJour.plusDays(1).minusNanos(1);
        return entreCreations(debutJour, finJour);
    }

    /**
     * Configure les critères pour rechercher les problèmes créés cette semaine
     */
    public ProblemeSearchCriteria creesCetteSemaine() {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime debutSemaine = maintenant.minusDays(maintenant.getDayOfWeek().getValue() - 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finSemaine = debutSemaine.plusDays(6).withHour(23).withMinute(59).withSecond(59);
        return entreCreations(debutSemaine, finSemaine);
    }

    /**
     * Configure les critères pour rechercher les problèmes créés ce mois
     */
    public ProblemeSearchCriteria creesCeMois() {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime debutMois = maintenant.withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finMois = debutMois.plusMonths(1).minusDays(1)
                .withHour(23).withMinute(59).withSecond(59);
        return entreCreations(debutMois, finMois);
    }

    /**
     * Configure les critères pour rechercher les problèmes urgents en cours
     */
    public ProblemeSearchCriteria urgentsEnCours() {
        return this.enCours().anciens(3); // Problèmes non résolus depuis plus de 3 jours
    }

    /**
     * Configure les critères pour rechercher par mot-clé dans la description
     */
    public ProblemeSearchCriteria avecMotCle(String motCle) {
        this.description = motCle;
        return this;
    }

    /**
     * Configure les critères pour rechercher les problèmes techniques (plomberie + électricité)
     */
    public ProblemeSearchCriteria techniques() {
        this.types = List.of(Probleme.Type.PLOMBERIE, Probleme.Type.ELECTRICITE);
        return this;
    }
}