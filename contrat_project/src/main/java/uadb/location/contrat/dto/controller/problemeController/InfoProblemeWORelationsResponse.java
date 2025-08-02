package uadb.location.contrat.dto.controller.problemeController;

import uadb.location.contrat.model.probleme.Probleme;

import java.time.LocalDateTime;

public record InfoProblemeWORelationsResponse(
        Integer id,
        Integer contratId,
        Integer signalePar,
        String description,
        Probleme.Type type,
        Probleme.Responsable responsable,
        Boolean resolu,
        LocalDateTime creeLe
) {
}
