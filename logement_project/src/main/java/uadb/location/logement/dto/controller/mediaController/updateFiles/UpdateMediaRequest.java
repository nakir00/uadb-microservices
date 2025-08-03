package uadb.location.logement.dto.controller.mediaController.updateFiles;

import jakarta.validation.constraints.NotBlank;

public record UpdateMediaRequest(
        @NotBlank(message = "La description est obligatoire")
        String description
) {
}
