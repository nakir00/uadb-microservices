package uadb.location.logement.dto.controller.maisonController.readMaisons;

import org.jetbrains.annotations.NotNull;
import uadb.location.logement.model.maison.Maison;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReadMaisonResponse {

    private Long id;
    private Long proprietaireId;
    private String adresse;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private LocalDateTime creeLe;

    public ReadMaisonResponse(Long id, Long proprietaireId, String adresse, BigDecimal latitude, BigDecimal longitude, String description, LocalDateTime creeLe /*, List<Chambre> chambres*/) {
        this.id = id;
        this.proprietaireId = proprietaireId;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.creeLe = creeLe;
    }

    public Long id() {
        return id;
    }

    public ReadMaisonResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public Long proprietaireId() {
        return proprietaireId;
    }

    public ReadMaisonResponse setProprietaireId(Long proprietaireId) {
        this.proprietaireId = proprietaireId;
        return this;
    }

    public String adresse() {
        return adresse;
    }

    public ReadMaisonResponse setAdresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public BigDecimal latitude() {
        return latitude;
    }

    public ReadMaisonResponse setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public BigDecimal longitude() {
        return longitude;
    }

    public ReadMaisonResponse setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public String description() {
        return description;
    }

    public ReadMaisonResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime creeLe() {
        return creeLe;
    }

    public ReadMaisonResponse setCreeLe(LocalDateTime creeLe) {
        this.creeLe = creeLe;
        return this;
    }

    public static @NotNull ReadMaisonResponse fromMaison(@NotNull Maison maison) {
        return new ReadMaisonResponse(maison.getId(), maison.getProprietaireId(), maison.getAdresse(), maison.getLatitude(), maison.getLongitude(), maison.getDescription(), maison.getCreeLe());

    }


}
