package uadb.location.logement.dto.controller.chambreController;

import uadb.location.logement.dto.controller.maisonController.InfoMaisonResponse;
import uadb.location.logement.dto.controller.mediaController.InfoMediaResponse;
import uadb.location.logement.dto.controller.rendezVousController.InfoRendezVousResponse;
import uadb.location.logement.dto.controller.rendezVousController.InfoRendezVousResponseWOChambre;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record InfoChambreResponse(Long id,
                                  String titre,
                                  String description,
                                  String taille,
                                  String type,
                                  boolean meublee,
                                  boolean salleDeBain,
                                  boolean disponible,
                                  BigDecimal prix,
                                  LocalDateTime creeLe,
                                  @Nullable InfoMaisonResponse maison,
                                  List<InfoRendezVousResponseWOChambre> rendezVous,
                                  List<InfoMediaResponse> medias
) {
}
