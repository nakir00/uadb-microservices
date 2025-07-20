package uadb.location.logement.dto.controller.chambreController;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record InfoChambreResponse(Long id,
                                  String titre,
                                  String description,
                                  String taille,
                                  String type,
                                  boolean meublee,
                                  boolean salleDeBain,
                                  boolean disponible,
                                  BigDecimal prix,
                                  LocalDateTime creeLe) {
}
