package uadb.location.contrat.dto.client.ChambreClient;


import uadb.location.contrat.dto.client.UtilisateurClient.ReadUtilisateurDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReadMaisonDTO(Long id,
                                Long proprietaireId,
                                String adresse,
                                BigDecimal latitude,
                                BigDecimal longitude,
                                String description,
                                LocalDateTime creeLe,
                                ReadUtilisateurDTO utilisateurDTO) {
}
