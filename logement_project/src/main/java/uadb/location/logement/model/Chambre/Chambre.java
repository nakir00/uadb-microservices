package uadb.location.logement.model.Chambre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import uadb.location.logement.dto.chambreController.InfoChambreResponse;
import uadb.location.logement.dto.chambreController.createChambre.CreateChambreRequest;
import uadb.location.logement.model.Media;
import uadb.location.logement.model.RendezVous;
import uadb.location.logement.model.maison.Maison;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "chambres")
@Data
public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String titre;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String taille;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(nullable = false)
    private boolean meublee;
    @Column(nullable = false)
    private boolean salleDeBain;
    @Column(nullable = false)
    private boolean disponible;
    @Column(nullable = false)
    private BigDecimal prix;
    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime creeLe;

    public Chambre(String titre, String description, String taille, Type type, boolean meublee, boolean salleDeBain, boolean disponible, BigDecimal prix) {
        this.titre = titre;
        this.description = description;
        this.taille = taille;
        this.type = type;
        this.meublee = meublee;
        this.salleDeBain = salleDeBain;
        this.disponible = disponible;
        this.prix = prix;
    }

    public Chambre(Long id, String titre, String description, String taille, Type type, boolean meublee, boolean salleDeBain, boolean disponible, BigDecimal prix, LocalDateTime creeLe, Maison maison, List<Media> medias, List<RendezVous> rendezVousList) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.taille = taille;
        this.type = type;
        this.meublee = meublee;
        this.salleDeBain = salleDeBain;
        this.disponible = disponible;
        this.prix = prix;
        this.creeLe = creeLe;
        this.maison = maison;
        this.medias = medias;
        this.rendezVousList = rendezVousList;
    }

    public Type type() {
        return type;
    }

    public Chambre setType(Type type) {
        this.type = type;
        return this;
    }

    public Maison maison() {
        return maison;
    }

    public Chambre setMaison(Maison maison) {
        this.maison = maison;
        return this;
    }

    public List<Media> medias() {
        return medias;
    }

    public Chambre setMedias(List<Media> medias) {
        this.medias = medias;
        return this;
    }

    public List<RendezVous> rendezVousList() {
        return rendezVousList;
    }

    public Chambre setRendezVousList(List<RendezVous> rendezVousList) {
        this.rendezVousList = rendezVousList;
        return this;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maison_id", nullable = false)
    @JsonBackReference
    private Maison maison;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chambre", cascade = CascadeType.ALL)
    private List<Media> medias;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chambre", cascade = CascadeType.ALL)
    private List<RendezVous> rendezVousList;

    public Long id() {
        return id;
    }

    public Chambre setId(Long id) {
        this.id = id;
        return this;
    }

    public String titre() {
        return titre;
    }

    public Chambre setTitre(String titre) {
        this.titre = titre;
        return this;
    }

    public String description() {
        return description;
    }

    public Chambre setDescription(String description) {
        this.description = description;
        return this;
    }

    public String taille() {
        return taille;
    }

    public Chambre setTaille(String taille) {
        this.taille = taille;
        return this;
    }

    public boolean meublee() {
        return meublee;
    }

    public Chambre setMeublee(boolean meublee) {
        this.meublee = meublee;
        return this;
    }

    public boolean salleDeBain() {
        return salleDeBain;
    }

    public Chambre setSalleDeBain(boolean salleDeBain) {
        this.salleDeBain = salleDeBain;
        return this;
    }

    public boolean disponible() {
        return disponible;
    }

    public Chambre setDisponible(boolean disponible) {
        this.disponible = disponible;
        return this;
    }

    public BigDecimal prix() {
        return prix;
    }

    public Chambre setPrix(BigDecimal prix) {
        this.prix = prix;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public Chambre setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    @Override
    public String toString() {
        return "Chambre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", taille='" + taille + '\'' +
                ", type=" + type +
                ", meublee=" + meublee +
                ", salleDeBain=" + salleDeBain +
                ", disponible=" + disponible +
                ", prix=" + prix +
                ", creeLe=" + creeLe +
                ", maison=" + maison +
                ", medias=" + medias +
                ", rendezVousList=" + rendezVousList +
                '}';
    }

    public enum Type {
        SIMPLE, APPARTEMENT, MAISON
    }

    public static final Chambre fromCreateChambreRequest(CreateChambreRequest createChambreRequest) {
        return new Chambre(
                createChambreRequest.titre(),
                createChambreRequest.description(),
                createChambreRequest.taille(),
                createChambreRequest.type(),
                createChambreRequest.meublee(),
                createChambreRequest.salleDeBain(),
                createChambreRequest.disponible(),
                createChambreRequest.prix());
    }

    public static InfoChambreResponse toChambreResponse(Chambre chambre) {
        return new InfoChambreResponse(
                chambre.id(),
                chambre.titre(),
                chambre.description(),
                chambre.taille(),
                chambre.type().name(),
                chambre.meublee(),
                chambre.salleDeBain(),
                chambre.disponible(),
                chambre.prix(),
                chambre.creeLe()
        );
    }
}
