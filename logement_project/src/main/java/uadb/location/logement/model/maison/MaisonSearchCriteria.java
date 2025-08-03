package uadb.location.logement.model.maison;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaisonSearchCriteria {

    private Long proprietaireId;
    private String adresse;
    private String description;
    private BigDecimal latMin;
    private BigDecimal latMax;
    private BigDecimal lonMin;
    private BigDecimal lonMax;
    private BigDecimal centerLat;
    private BigDecimal centerLon;
    private Double radiusKm;
    private LocalDateTime dateCreationMin;
    private LocalDateTime dateCreationMax;
    private Integer minChambres;
    private Integer maxChambres;
    private Boolean hasChambresDisponibles;
    private Double prixMinChambres;
    private Double prixMaxChambres;
    private String typeChambre;

    // Constructeur par défaut
    public MaisonSearchCriteria() {}

    // Getters et Setters
    public Long getProprietaireId() { return proprietaireId; }
    public void setProprietaireId(Long proprietaireId) { this.proprietaireId = proprietaireId; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getLatMin() { return latMin; }
    public void setLatMin(BigDecimal latMin) { this.latMin = latMin; }

    public BigDecimal getLatMax() { return latMax; }
    public void setLatMax(BigDecimal latMax) { this.latMax = latMax; }

    public BigDecimal getLonMin() { return lonMin; }
    public void setLonMin(BigDecimal lonMin) { this.lonMin = lonMin; }

    public BigDecimal getLonMax() { return lonMax; }
    public void setLonMax(BigDecimal lonMax) { this.lonMax = lonMax; }

    public BigDecimal getCenterLat() { return centerLat; }
    public void setCenterLat(BigDecimal centerLat) { this.centerLat = centerLat; }

    public BigDecimal getCenterLon() { return centerLon; }
    public void setCenterLon(BigDecimal centerLon) { this.centerLon = centerLon; }

    public Double getRadiusKm() { return radiusKm; }
    public void setRadiusKm(Double radiusKm) { this.radiusKm = radiusKm; }

    public LocalDateTime getDateCreationMin() { return dateCreationMin; }
    public void setDateCreationMin(LocalDateTime dateCreationMin) { this.dateCreationMin = dateCreationMin; }

    public LocalDateTime getDateCreationMax() { return dateCreationMax; }
    public void setDateCreationMax(LocalDateTime dateCreationMax) { this.dateCreationMax = dateCreationMax; }

    public Integer getMinChambres() { return minChambres; }
    public void setMinChambres(Integer minChambres) { this.minChambres = minChambres; }

    public Integer getMaxChambres() { return maxChambres; }
    public void setMaxChambres(Integer maxChambres) { this.maxChambres = maxChambres; }

    public Boolean getHasChambresDisponibles() { return hasChambresDisponibles; }
    public void setHasChambresDisponibles(Boolean hasChambresDisponibles) { this.hasChambresDisponibles = hasChambresDisponibles; }

    public Double getPrixMinChambres() { return prixMinChambres; }
    public void setPrixMinChambres(Double prixMinChambres) { this.prixMinChambres = prixMinChambres; }

    public Double getPrixMaxChambres() { return prixMaxChambres; }
    public void setPrixMaxChambres(Double prixMaxChambres) { this.prixMaxChambres = prixMaxChambres; }

    public String getTypeChambre() { return typeChambre; }
    public void setTypeChambre(String typeChambre) { this.typeChambre = typeChambre; }

}
