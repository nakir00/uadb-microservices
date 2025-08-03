package uadb.location.contrat.model.probleme;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProblemeSpecification {

    public static Specification<Probleme> withCriteria(ProblemeSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Recherche par ID exact
            if (criteria.id() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), criteria.id()));
            }

            // Recherche par contrat ID
            if (criteria.contratId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("contratId"), criteria.contratId()));
            }

            // Recherche par liste de contrat IDs
            if (criteria.contratIds() != null && !criteria.contratIds().isEmpty()) {
                predicates.add(root.get("contratId").in(criteria.contratIds()));
            }

            // Recherche par signalé par
            if (criteria.signalePar() != null) {
                predicates.add(criteriaBuilder.equal(root.get("signalePar"), criteria.signalePar()));
            }

            // Recherche par liste de personnes qui ont signalé
            if (criteria.signaleParList() != null && !criteria.signaleParList().isEmpty()) {
                predicates.add(root.get("signalePar").in(criteria.signaleParList()));
            }

            // Recherche par description (contient le texte)
            if (criteria.description() != null && !criteria.description().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + criteria.description().toLowerCase() + "%"
                ));
            }

            // Recherche par type unique
            if (criteria.type() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), criteria.type()));
            }

            // Recherche par liste de types
            if (criteria.types() != null && !criteria.types().isEmpty()) {
                predicates.add(root.get("type").in(criteria.types()));
            }

            // Recherche par responsable unique
            if (criteria.responsable() != null) {
                predicates.add(criteriaBuilder.equal(root.get("responsable"), criteria.responsable()));
            }

            // Recherche par liste de responsables
            if (criteria.responsables() != null && !criteria.responsables().isEmpty()) {
                predicates.add(root.get("responsable").in(criteria.responsables()));
            }

            // Recherche par statut de résolution
            if (criteria.resolu() != null) {
                predicates.add(criteriaBuilder.equal(root.get("resolu"), criteria.resolu()));
            }

            // Recherche par date de création exacte
            if (criteria.creeLe() != null) {
                predicates.add(criteriaBuilder.equal(root.get("creeLe"), criteria.creeLe()));
            }

            // Recherche par plage de dates de création
            if (criteria.creeLeFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), criteria.creeLeFrom()));
            }
            if (criteria.creeLeTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), criteria.creeLeTo()));
            }

            // Critères avancés

            // Recherche par statut en cours/résolu (alternative au Boolean resolu)
            if (criteria.isEnCours() != null && criteria.isEnCours()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("resolu"), false),
                        criteriaBuilder.isNull(root.get("resolu"))
                ));
            }

            if (criteria.isResolu() != null && criteria.isResolu()) {
                predicates.add(criteriaBuilder.equal(root.get("resolu"), true));
            }

            // Recherche par nombre de jours depuis création
            if (criteria.nombreJoursDepuisCreation() != null) {
                LocalDateTime dateReference = LocalDateTime.now().minusDays(criteria.nombreJoursDepuisCreation());
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.function("DATE", java.sql.Date.class, root.get("creeLe")),
                        criteriaBuilder.function("DATE", java.sql.Date.class, criteriaBuilder.literal(dateReference))
                ));
            }

            // Recherche par nombre minimum de jours depuis création
            if (criteria.nombreJoursDepuisCreationMin() != null) {
                LocalDateTime dateMax = LocalDateTime.now().minusDays(criteria.nombreJoursDepuisCreationMin());
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), dateMax));
            }

            // Recherche par nombre maximum de jours depuis création
            if (criteria.nombreJoursDepuisCreationMax() != null) {
                LocalDateTime dateMin = LocalDateTime.now().minusDays(criteria.nombreJoursDepuisCreationMax());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), dateMin));
            }

            // Créé avant une date
            if (criteria.creeAvant() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("creeLe"), criteria.creeAvant()));
            }

            // Créé après une date
            if (criteria.creeApres() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("creeLe"), criteria.creeApres()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Méthodes utilitaires pour des spécifications spécifiques

    public static Specification<Probleme> hasId(Integer id) {
        return (root, query, criteriaBuilder) ->
                id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<Probleme> hasContratId(Integer contratId) {
        return (root, query, criteriaBuilder) ->
                contratId != null ? criteriaBuilder.equal(root.get("contratId"), contratId) : null;
    }

    public static Specification<Probleme> hasContratIds(List<Integer> contratIds) {
        return (root, query, criteriaBuilder) ->
                contratIds != null && !contratIds.isEmpty() ? root.get("contratId").in(contratIds) : null;
    }

    public static Specification<Probleme> hasType(Probleme.Type type) {
        return (root, query, criteriaBuilder) ->
                type != null ? criteriaBuilder.equal(root.get("type"), type) : null;
    }

    public static Specification<Probleme> hasTypes(List<Probleme.Type> types) {
        return (root, query, criteriaBuilder) ->
                types != null && !types.isEmpty() ? root.get("type").in(types) : null;
    }

    public static Specification<Probleme> hasResponsable(Probleme.Responsable responsable) {
        return (root, query, criteriaBuilder) ->
                responsable != null ? criteriaBuilder.equal(root.get("responsable"), responsable) : null;
    }

    public static Specification<Probleme> hasResponsables(List<Probleme.Responsable> responsables) {
        return (root, query, criteriaBuilder) ->
                responsables != null && !responsables.isEmpty() ? root.get("responsable").in(responsables) : null;
    }

    public static Specification<Probleme> isResolu() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("resolu"), true);
    }

    public static Specification<Probleme> isEnCours() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("resolu"), false),
                        criteriaBuilder.isNull(root.get("resolu"))
                );
    }

    public static Specification<Probleme> isPlomberie() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), Probleme.Type.PLOMBERIE);
    }

    public static Specification<Probleme> isElectricite() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), Probleme.Type.ELECTRICITE);
    }

    public static Specification<Probleme> isAutre() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), Probleme.Type.AUTRE);
    }

    public static Specification<Probleme> isTechnique() {
        return (root, query, criteriaBuilder) ->
                root.get("type").in(List.of(Probleme.Type.PLOMBERIE, Probleme.Type.ELECTRICITE));
    }

    public static Specification<Probleme> responsabiliteLocataire() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("responsable"), Probleme.Responsable.LOCATAIRE);
    }

    public static Specification<Probleme> responsabiliteProprietaire() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("responsable"), Probleme.Responsable.PROPRIETAIRE);
    }

    public static Specification<Probleme> signalePar(Integer personneId) {
        return (root, query, criteriaBuilder) ->
                personneId != null ? criteriaBuilder.equal(root.get("signalePar"), personneId) : null;
    }

    public static Specification<Probleme> descriptionContient(String motCle) {
        return (root, query, criteriaBuilder) ->
                motCle != null && !motCle.trim().isEmpty() ?
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                                "%" + motCle.toLowerCase() + "%") : null;
    }

    public static Specification<Probleme> creeEntre(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get("creeLe"), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), from);
            } else if (to != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), to);
            }
            return null;
        };
    }

    public static Specification<Probleme> creeAujourdhui() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime debutJour = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime finJour = debutJour.plusDays(1).minusNanos(1);
            return criteriaBuilder.between(root.get("creeLe"), debutJour, finJour);
        };
    }

    public static Specification<Probleme> creeHier() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime debutHier = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime finHier = debutHier.plusDays(1).minusNanos(1);
            return criteriaBuilder.between(root.get("creeLe"), debutHier, finHier);
        };
    }

    public static Specification<Probleme> creeDepuisXJours(int nombreJours) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime dateReference = LocalDateTime.now().minusDays(nombreJours);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), dateReference);
        };
    }

    public static Specification<Probleme> creePlusAncienQueXJours(int nombreJours) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime dateReference = LocalDateTime.now().minusDays(nombreJours);
            return criteriaBuilder.lessThan(root.get("creeLe"), dateReference);
        };
    }

    public static Specification<Probleme> creeeCetteSemaine() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime maintenant = LocalDateTime.now();
            LocalDateTime debutSemaine = maintenant.minusDays(maintenant.getDayOfWeek().getValue() - 1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime finSemaine = debutSemaine.plusDays(6).withHour(23).withMinute(59).withSecond(59);
            return criteriaBuilder.between(root.get("creeLe"), debutSemaine, finSemaine);
        };
    }

    public static Specification<Probleme> creeeCeMois() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime maintenant = LocalDateTime.now();
            LocalDateTime debutMois = maintenant.withDayOfMonth(1)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime finMois = debutMois.plusMonths(1).minusDays(1)
                    .withHour(23).withMinute(59).withSecond(59);
            return criteriaBuilder.between(root.get("creeLe"), debutMois, finMois);
        };
    }

    public static Specification<Probleme> urgents() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime il3Jours = LocalDateTime.now().minusDays(3);
            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("resolu"), false),
                            criteriaBuilder.isNull(root.get("resolu"))
                    ),
                    criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), il3Jours)
            );
        };
    }

    public static Specification<Probleme> techniques() {
        return (root, query, criteriaBuilder) ->
                root.get("type").in(List.of(Probleme.Type.PLOMBERIE, Probleme.Type.ELECTRICITE));
    }

    // Spécifications pour jointures avec Contrat (si nécessaire)

    public static Specification<Probleme> contratActif() {
        return (root, query, criteriaBuilder) -> {
            var contratJoin = root.join("contrat");
            return criteriaBuilder.equal(contratJoin.get("statut"), "ACTIF"); // Assumant que Contrat a un statut ACTIF
        };
    }

    public static Specification<Probleme> contratLocataireId(Long locataireId) {
        return (root, query, criteriaBuilder) -> {
            if (locataireId != null) {
                var contratJoin = root.join("contrat");
                return criteriaBuilder.equal(contratJoin.get("locataireId"), locataireId);
            }
            return null;
        };
    }

    public static Specification<Probleme> contratChambreId(Long chambreId) {
        return (root, query, criteriaBuilder) -> {
            if (chambreId != null) {
                var contratJoin = root.join("contrat");
                return criteriaBuilder.equal(contratJoin.get("chambreId"), chambreId);
            }
            return null;
        };
    }

    // Statistiques et regroupements utiles

    public static Specification<Probleme> problemesMajeurs() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime uneSemine = LocalDateTime.now().minusDays(7);
            return criteriaBuilder.and(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("resolu"), false),
                            criteriaBuilder.isNull(root.get("resolu"))
                    ),
                    criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), uneSemine),
                    root.get("type").in(List.of(Probleme.Type.PLOMBERIE, Probleme.Type.ELECTRICITE))
            );
        };
    }
}