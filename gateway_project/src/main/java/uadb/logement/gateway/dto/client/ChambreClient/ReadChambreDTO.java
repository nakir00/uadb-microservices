package uadb.location.contrat.dto.client.ChambreClient;

import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReadChambreDTO(Long id,
                                  String titre,
                                  String description,
                                  String taille,
                                  String type,
                                  boolean meublee,
                                  boolean salleDeBain,
                                  boolean disponible,
                                  BigDecimal prix,
                                  LocalDateTime creeLe,
                                  @Nullable ReadMaisonDTO maison,
                                  List<ReadRendezVousDTO> rendezVous,
                                  List<ReadMediaDTO> medias
) {
}
