package uadb.location.logement.dto.controller.maisonController.createMaison;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public final class CreateMaisonRequest {
    private final String adresse;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String description;

    public CreateMaisonRequest(String adresse,
                               BigDecimal latitude,
                               BigDecimal longitude,
                               String description) {
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }


    public String getAdresse() {
        return adresse;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "CreateMaisonRequest{" +
                "adresse='" + adresse + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                '}';
    }
}
