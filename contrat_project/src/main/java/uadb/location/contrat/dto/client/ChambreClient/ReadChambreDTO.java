package uadb.location.contrat.dto.client.UtilisateurClient;

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
