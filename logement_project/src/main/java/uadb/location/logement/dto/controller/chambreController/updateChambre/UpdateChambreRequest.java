package uadb.location.logement.dto.controller.chambreController.updateChambre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import uadb.location.logement.model.Chambre.Chambre;

import java.math.BigDecimal;

public record UpdateChambreRequest(
        @NotBlank(message = "L'id est obligatoire")
        Long id,

        @NotBlank(message = "Le titre est obligatoire")
        String titre,

        @NotBlank(message = "La description est obligatoire")
        String description,

        @NotBlank(message = "La taille est obligatoire")
        String taille,

        @NotNull(message = "Le type est obligatoire")
        Chambre.Type type,

        @NotNull(message = "L'information meublée est obligatoire")
        Boolean meublee,

        @NotNull(message = "L'information salle de bain est obligatoire")
        Boolean salleDeBain,

        @NotNull(message = "Le statut de disponibilité est obligatoire")
        Boolean disponible,

        @NotNull(message = "Le prix est obligatoire")
        @Positive(message = "Le prix doit être positif")
        BigDecimal prix
) {
}