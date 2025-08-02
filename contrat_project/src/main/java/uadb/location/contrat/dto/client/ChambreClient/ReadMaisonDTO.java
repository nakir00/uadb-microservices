package uadb.location.contrat.dto.client.ChambreClient;

import java.time.LocalDateTime;

public record ReadMaisonDTO(
        Long id,
        String nomUtilisateur,
        String email,
        String telephone,
        String CNI,
        String role,
        LocalDateTime creeLe) {

}
