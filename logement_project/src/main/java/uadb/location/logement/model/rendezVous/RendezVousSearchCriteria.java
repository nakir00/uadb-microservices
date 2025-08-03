package uadb.location.logement.model.rendezVous;

import lombok.Builder;
import lombok.Data;
import uadb.location.logement.model.Chambre.Chambre;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class RendezVousSearchCriteria {

    // Critères directs sur RendezVous
    private Long locataireId;
    private LocalDateTime dateHeureMin;
    private LocalDateTime dateHeureMax;
    private RendezVous.Statut statut;

    // Critères sur les dates de création
    private LocalDateTime creeLeSince;
    private LocalDateTime creeLeUntil;

    // Critères sur la chambre associée
    private Long chambreId;
    private String chambreTitre;
    private String chambreDescription;
    private String chambreTaille;
    private uadb.location.logement.model.Chambre.Chambre.Type chambreType;
    private Boolean chambreMeublee;
    private Boolean chambreSalleDeBain;
    private Boolean chambreDisponible;

    // Critères sur la maison (via chambre)
    private Long maisonId;
    private String maisonNom;
    private String maisonVille;
    private String maisonQuartier;

    // Critère pour propriétaire (via chambre -> maison)
    private Long proprietaireId;

    // Critères utilitaires
    private Boolean hasDateHeure; // Pour filtrer les rendez-vous avec ou sans heure définie
    private Integer recentDays; // Pour les rendez-vous récents (N derniers jours)

    public RendezVousSearchCriteria(Long locataireId, LocalDateTime dateHeureMin, LocalDateTime dateHeureMax, RendezVous.Statut statut, LocalDateTime creeLeSince, LocalDateTime creeLeUntil, Long chambreId, String chambreTitre, String chambreDescription, String chambreTaille, Chambre.Type chambreType, Boolean chambreMeublee, Boolean chambreSalleDeBain, Boolean chambreDisponible, Long maisonId, String maisonNom, String maisonVille, String maisonQuartier, Long proprietaireId, Boolean hasDateHeure, Integer recentDays) {
        this.locataireId = locataireId;
        this.dateHeureMin = dateHeureMin;
        this.dateHeureMax = dateHeureMax;
        this.statut = statut;
        this.creeLeSince = creeLeSince;
        this.creeLeUntil = creeLeUntil;
        this.chambreId = chambreId;
        this.chambreTitre = chambreTitre;
        this.chambreDescription = chambreDescription;
        this.chambreTaille = chambreTaille;
        this.chambreType = chambreType;
        this.chambreMeublee = chambreMeublee;
        this.chambreSalleDeBain = chambreSalleDeBain;
        this.chambreDisponible = chambreDisponible;
        this.maisonId = maisonId;
        this.maisonNom = maisonNom;
        this.maisonVille = maisonVille;
        this.maisonQuartier = maisonQuartier;
        this.proprietaireId = proprietaireId;
        this.hasDateHeure = hasDateHeure;
        this.recentDays = recentDays;
    }

    public RendezVousSearchCriteria() {
    }

    public Long locataireId() {
        return locataireId;
    }

    public RendezVousSearchCriteria setLocataireId(Long locataireId) {
        this.locataireId = locataireId;
        return this;
    }

    public LocalDateTime dateHeureMin() {
        return dateHeureMin;
    }

    public RendezVousSearchCriteria setDateHeureMin(LocalDateTime dateHeureMin) {
        this.dateHeureMin = dateHeureMin;
        return this;
    }

    public LocalDateTime dateHeureMax() {
        return dateHeureMax;
    }

    public RendezVousSearchCriteria setDateHeureMax(LocalDateTime dateHeureMax) {
        this.dateHeureMax = dateHeureMax;
        return this;
    }

    public RendezVous.Statut statut() {
        return statut;
    }

    public RendezVousSearchCriteria setStatut(RendezVous.Statut statut) {
        this.statut = statut;
        return this;
    }

    public LocalDateTime creeLeSince() {
        return creeLeSince;
    }

    public RendezVousSearchCriteria setCreeLeSince(LocalDateTime creeLeSince) {
        this.creeLeSince = creeLeSince;
        return this;
    }

    public LocalDateTime creeLeUntil() {
        return creeLeUntil;
    }

    public RendezVousSearchCriteria setCreeLeUntil(LocalDateTime creeLeUntil) {
        this.creeLeUntil = creeLeUntil;
        return this;
    }

    public Long chambreId() {
        return chambreId;
    }

    public RendezVousSearchCriteria setChambreId(Long chambreId) {
        this.chambreId = chambreId;
        return this;
    }

    public String chambreTitre() {
        return chambreTitre;
    }

    public RendezVousSearchCriteria setChambreTitre(String chambreTitre) {
        this.chambreTitre = chambreTitre;
        return this;
    }

    public String chambreDescription() {
        return chambreDescription;
    }

    public RendezVousSearchCriteria setChambreDescription(String chambreDescription) {
        this.chambreDescription = chambreDescription;
        return this;
    }

    public String chambreTaille() {
        return chambreTaille;
    }

    public RendezVousSearchCriteria setChambreTaille(String chambreTaille) {
        this.chambreTaille = chambreTaille;
        return this;
    }

    public Chambre.Type chambreType() {
        return chambreType;
    }

    public RendezVousSearchCriteria setChambreType(Chambre.Type chambreType) {
        this.chambreType = chambreType;
        return this;
    }

    public Boolean chambreMeublee() {
        return chambreMeublee;
    }

    public RendezVousSearchCriteria setChambreMeublee(Boolean chambreMeublee) {
        this.chambreMeublee = chambreMeublee;
        return this;
    }

    public Boolean chambreSalleDeBain() {
        return chambreSalleDeBain;
    }

    public RendezVousSearchCriteria setChambreSalleDeBain(Boolean chambreSalleDeBain) {
        this.chambreSalleDeBain = chambreSalleDeBain;
        return this;
    }

    public Boolean chambreDisponible() {
        return chambreDisponible;
    }

    public RendezVousSearchCriteria setChambreDisponible(Boolean chambreDisponible) {
        this.chambreDisponible = chambreDisponible;
        return this;
    }

    public Long maisonId() {
        return maisonId;
    }

    public RendezVousSearchCriteria setMaisonId(Long maisonId) {
        this.maisonId = maisonId;
        return this;
    }

    public String maisonNom() {
        return maisonNom;
    }

    public RendezVousSearchCriteria setMaisonNom(String maisonNom) {
        this.maisonNom = maisonNom;
        return this;
    }

    public String maisonVille() {
        return maisonVille;
    }

    public RendezVousSearchCriteria setMaisonVille(String maisonVille) {
        this.maisonVille = maisonVille;
        return this;
    }

    public String maisonQuartier() {
        return maisonQuartier;
    }

    public RendezVousSearchCriteria setMaisonQuartier(String maisonQuartier) {
        this.maisonQuartier = maisonQuartier;
        return this;
    }

    public Long proprietaireId() {
        return proprietaireId;
    }

    public RendezVousSearchCriteria setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
        return this;
    }

    public Boolean hasDateHeure() {
        return hasDateHeure;
    }

    public RendezVousSearchCriteria setHasDateHeure(Boolean hasDateHeure) {
        this.hasDateHeure = hasDateHeure;
        return this;
    }

    public Integer recentDays() {
        return recentDays;
    }

    public RendezVousSearchCriteria setRecentDays(Integer recentDays) {
        this.recentDays = recentDays;
        return this;
    }
}