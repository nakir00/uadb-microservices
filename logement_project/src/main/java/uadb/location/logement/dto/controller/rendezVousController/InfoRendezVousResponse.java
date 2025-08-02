package uadb.location.logement.dto.controller.rendezVousController;

import java.time.LocalDateTime;

public record InfoRendezVousResponse(
        Long id,
        Long locataireId,
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime dateHeure,
        RendezVous.Statut statut,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime creeLe,
        ChambreInfoResponse chambre
) {}