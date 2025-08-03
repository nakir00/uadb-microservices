package uadb.location.contrat.dto.controller.problemeController.saveProbleme;

import uadb.location.contrat.model.probleme.Probleme;

public record CreateProblemeRequest(
        Long contratId,
        Long signalePar,
        String description,
        Probleme.Type type,
        Probleme.Responsable responsable,
        boolean resolu

) {
}
