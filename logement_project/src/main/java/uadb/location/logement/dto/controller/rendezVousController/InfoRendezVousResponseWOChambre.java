package uadb.location.logement.dto.controller.rendezVousController;

import com.fasterxml.jackson.annotation.JsonFormat;
import uadb.location.logement.dto.controller.chambreController.InfoChambreResponse;
import uadb.location.logement.model.rendezVous.RendezVous;

import java.time.LocalDateTime;

public record InfoRendezVousResponseWOChambre(
        Long id,
        Long locataireId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime dateHeure,
        RendezVous.Statut statut,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime creeLe
) {}