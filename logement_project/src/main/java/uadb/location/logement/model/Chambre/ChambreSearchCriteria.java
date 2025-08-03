package uadb.location.logement.model.Chambre;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ChambreSearchCriteria {
    private String titre;
    private String description;
    private String taille;
    private Chambre.Type type;
    private Boolean meublee;
    private Boolean salleDeBain;
    private Boolean disponible;
    private BigDecimal prixMin;
    private BigDecimal prixMax;
    private LocalDateTime creeLeSince;
    private LocalDateTime creeLeUntil;
    private Long maisonId;
    private String maisonNom;
    private String maisonVille;
    private String maisonQuartier;
    private Long proprietaireId;

    // Constructeur par défaut
    public ChambreSearchCriteria() {}

    // Constructeur avec paramètres principaux
    public ChambreSearchCriteria(String titre, Chambre.Type type, Boolean disponible,
                                 BigDecimal prixMin, BigDecimal prixMax) {
        this.titre = titre;
        this.type = type;
        this.disponible = disponible;
        this.prixMin = prixMin;
        this.prixMax = prixMax;
    }

    // Getters et Setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTaille() { return taille; }
    public void setTaille(String taille) { this.taille = taille; }

    public Chambre.Type getType() { return type; }
    public void setType(Chambre.Type type) { this.type = type; }

    public Boolean getMeublee() { return meublee; }
    public void setMeublee(Boolean meublee) { this.meublee = meublee; }

    public Boolean getSalleDeBain() { return salleDeBain; }
    public void setSalleDeBain(Boolean salleDeBain) { this.salleDeBain = salleDeBain; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public BigDecimal getPrixMin() { return prixMin; }
    public void setPrixMin(BigDecimal prixMin) { this.prixMin = prixMin; }

    public BigDecimal getPrixMax() { return prixMax; }
    public void setPrixMax(BigDecimal prixMax) { this.prixMax = prixMax; }

    public LocalDateTime getCreeLeSince() { return creeLeSince; }
    public void setCreeLeSince(LocalDateTime creeLeSince) { this.creeLeSince = creeLeSince; }

    public LocalDateTime getCreeLeUntil() { return creeLeUntil; }
    public void setCreeLeUntil(LocalDateTime creeLeUntil) { this.creeLeUntil = creeLeUntil; }

    public Long getMaisonId() { return maisonId; }
    public void setMaisonId(Long maisonId) { this.maisonId = maisonId; }

    public String getMaisonNom() { return maisonNom; }
    public void setMaisonNom(String maisonNom) { this.maisonNom = maisonNom; }

    public String getMaisonVille() { return maisonVille; }
    public void setMaisonVille(String maisonVille) { this.maisonVille = maisonVille; }

    public String getMaisonQuartier() { return maisonQuartier; }
    public void setMaisonQuartier(String maisonQuartier) { this.maisonQuartier = maisonQuartier; }

    public Long proprietaireId() {
        return proprietaireId;
    }

    public ChambreSearchCriteria setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
        return this;
    }
}