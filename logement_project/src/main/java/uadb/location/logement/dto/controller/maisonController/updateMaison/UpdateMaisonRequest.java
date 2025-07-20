package uadb.location.logement.dto.controller.maisonController.updateMaison;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateMaisonRequest(
                                  String adresse,
                                  BigDecimal latitude,
                                  BigDecimal longitude,
                                  String description) {
}
