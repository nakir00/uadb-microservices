package uadb.location.logement.dto.client.UtilisateurClient;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record ReadUtilisateurDTO(
        Long id,
        String nomUtilisateur,
        String email,
        String telephone,
        String CNI,
        String role,
        LocalDateTime creeLe) {

}
