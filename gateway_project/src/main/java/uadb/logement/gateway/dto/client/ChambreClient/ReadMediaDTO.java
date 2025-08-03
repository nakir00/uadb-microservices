package uadb.location.contrat.dto.client.ChambreClient;

import java.time.LocalDateTime;

public record ReadMediaDTO (
        Long id,
        String url,
        String type,
        String description,
        LocalDateTime creeLe
){
}
