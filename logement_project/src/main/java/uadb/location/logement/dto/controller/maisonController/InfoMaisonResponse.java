package uadb.location.logement.dto.controller.maisonController;

import uadb.location.logement.dto.client.UtilisateurClient.ReadUtilisateurDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InfoMaisonResponse(Long id,
                                 Long proprietaireId,
                                 String adresse,
                                 BigDecimal latitude,
                                 BigDecimal longitude,
                                 String description,
                                 LocalDateTime creeLe,
                                 ReadUtilisateurDTO utilisateurDTO) {
}
