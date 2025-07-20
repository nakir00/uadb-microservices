package uadb.location.logement.dto.controller.maisonController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InfoMaisonResponse(Long id,
                                 Long proprietaireId,
                                 String adresse,
                                 BigDecimal latitude,
                                 BigDecimal longitude,
                                 String description,
                                 LocalDateTime creeLe) {
}
