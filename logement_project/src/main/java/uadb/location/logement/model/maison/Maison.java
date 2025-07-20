package uadb.location.logement.model.maison;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.logement.model.Chambre.Chambre;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maisons")
@Builder
@Data
public class Maison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long proprietaireId;
    private String adresse;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;

    @CreationTimestamp(source = SourceType.DB)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creeLe;

    public Maison(String description, BigDecimal longitude, BigDecimal latitude, String adresse, Long proprietaireId) {
        this.proprietaireId = proprietaireId;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public Maison(Long proprietaireId, String adresse, BigDecimal latitude, BigDecimal longitude, String description) {
        this.proprietaireId = proprietaireId;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public Maison(Long id, Long proprietaireId, String adresse, BigDecimal latitude, BigDecimal longitude, String description) {
        this.id = id;
        this.proprietaireId = proprietaireId;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maison", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Chambre> chambres;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }



    public LocalDateTime getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
    }

    public List<Chambre> getChambres() {
        return chambres;
    }

    public void setChambres(List<Chambre> chambres) {
        this.chambres = chambres;
    }


    @Override
    public String toString() {
        return "Maison{" +
                "id=" + id +
                ", proprietaireId=" + proprietaireId +
                ", adresse='" + adresse + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", creeLe=" + creeLe +
                ", chambres=" + chambres +
                '}';
    }
}
