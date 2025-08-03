package uadb.logement.gateway.dto.utilisateur;

import java.time.LocalDateTime;

public record InfoUtilisateurResponse(
        Long id,
        String nomUtilisateur,
        String email,
        String telephone,
        String CNI,
        String role,
        LocalDateTime creeLe) {
}
