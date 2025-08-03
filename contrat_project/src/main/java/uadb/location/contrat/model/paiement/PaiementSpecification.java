package uadb.location.contrat.model.paiement;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import uadb.location.contrat.model.contrat.Contrat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaiementSpecification {

    public static Specification<Paiement> withCriteria(PaiementSearchCriteria criteria) {
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

            // Recherche par montant exact
            if (criteria.montant() != null) {
                predicates.add(criteriaBuilder.equal(root.get("montant"), criteria.montant()));
            }

            // Recherche par plage de montants
            if (criteria.montantMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("montant"), criteria.montantMin()));
            }
            if (criteria.montantMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("montant"), criteria.montantMax()));
            }

            // Recherche par statut unique
            if (criteria.statut() != null && !criteria.statut().name().trim().isEmpty()) {
                try {
                    Paiement.Statut statut = Paiement.Statut.valueOf(criteria.statut().name().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("statut"), statut));
                } catch (IllegalArgumentException ignored) {
                    // Statut invalide, on ignore ce critère
                }
            }

            // Recherche par liste de statuts
            if (criteria.statuts() != null && !criteria.statuts().isEmpty()) {
                List<Paiement.Statut> validStatuts = new ArrayList<>();
                for (String statut : criteria.statuts()) {
                    try {
                        validStatuts.add(Paiement.Statut.valueOf(statut.toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                        // Statut invalide, on l'ignore
                    }
                }
                if (!validStatuts.isEmpty()) {
                    predicates.add(root.get("statut").in(validStatuts));
                }
            }

            // Recherche par date d'échéance exacte
            if (criteria.dateEcheance() != null) {
                predicates.add(criteriaBuilder.equal(root.get("dateEcheance"), criteria.dateEcheance()));
            }

            // Recherche par plage de dates d'échéance
            if (criteria.dateEcheanceFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateEcheance"), criteria.dateEcheanceFrom()));
            }
            if (criteria.dateEcheanceTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateEcheance"), criteria.dateEcheanceTo()));
            }

            // Recherche par date de paiement exacte
            if (criteria.datePaiement() != null) {
                predicates.add(criteriaBuilder.equal(root.get("datePaiement"), criteria.datePaiement()));
            }

            // Recherche par plage de dates de paiement
            if (criteria.datePaiementFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("datePaiement"), criteria.datePaiementFrom()));
            }
            if (criteria.datePaiementTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("datePaiement"), criteria.datePaiementTo()));
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

            // Recherche par statut payé/impayé (alternative au statut string)
            if (criteria.isPaye() != null) {
                Paiement.Statut statutCible = criteria.isPaye() ? Paiement.Statut.PAYE : Paiement.Statut.IMPAYE;
                predicates.add(criteriaBuilder.equal(root.get("statut"), statutCible));
            }

            // Recherche des paiements en retard
            if (criteria.isEnRetard() != null && criteria.isEnRetard()) {
                LocalDate aujourdhui = LocalDate.now();
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("statut"), Paiement.Statut.IMPAYE),
                        criteriaBuilder.lessThan(root.get("dateEcheance"), aujourdhui)
                ));
            }

            if (criteria.chambreIds() != null && !criteria.chambreIds().isEmpty()) {
                // Join avec la table Contrat pour accéder à chambreId
                Join<Paiement, Contrat> contratJoin = root.join("contrat", JoinType.INNER);
                predicates.add(contratJoin.get("chambreId").in(criteria.chambreIds()));
            }

            // Échéance avant une date
            if (criteria.echeanceAvant() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("dateEcheance"), criteria.echeanceAvant()));
            }

            // Échéance après une date
            if (criteria.echeanceApres() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("dateEcheance"), criteria.echeanceApres()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Méthodes utilitaires pour des spécifications spécifiques

    public static Specification<Paiement> hasId(Integer id) {
        return (root, query, criteriaBuilder) ->
                id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<Paiement> hasContratId(Integer contratId) {
        return (root, query, criteriaBuilder) ->
                contratId != null ? criteriaBuilder.equal(root.get("contratId"), contratId) : null;
    }

    public static Specification<Paiement> hasContratIds(List<Integer> contratIds) {
        return (root, query, criteriaBuilder) ->
                contratIds != null && !contratIds.isEmpty() ? root.get("contratId").in(contratIds) : null;
    }

    public static Specification<Paiement> hasStatut(Paiement.Statut statut) {
        return (root, query, criteriaBuilder) ->
                statut != null ? criteriaBuilder.equal(root.get("statut"), statut) : null;
    }

    public static Specification<Paiement> isPaye() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("statut"), Paiement.Statut.PAYE);
    }

    public static Specification<Paiement> isImpaye() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("statut"), Paiement.Statut.IMPAYE);
    }

    public static Specification<Paiement> isEnRetard() {
        return (root, query, criteriaBuilder) -> {
            LocalDate aujourdhui = LocalDate.now();
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("statut"), Paiement.Statut.IMPAYE),
                    criteriaBuilder.lessThan(root.get("dateEcheance"), aujourdhui)
            );
        };
    }

    public static Specification<Paiement> isAVenir() {
        return (root, query, criteriaBuilder) -> {
            LocalDate aujourdhui = LocalDate.now();
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("statut"), Paiement.Statut.IMPAYE),
                    criteriaBuilder.greaterThan(root.get("dateEcheance"), aujourdhui)
            );
        };
    }

    public static Specification<Paiement> echeanceEntre(LocalDate from, LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get("dateEcheance"), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateEcheance"), from);
            } else if (to != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dateEcheance"), to);
            }
            return null;
        };
    }

    public static Specification<Paiement> paiementEntre(java.time.LocalDateTime from, java.time.LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get("datePaiement"), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("datePaiement"), from);
            } else if (to != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("datePaiement"), to);
            }
            return null;
        };
    }

    public static Specification<Paiement> montantEntre(java.math.BigDecimal min, java.math.BigDecimal max) {
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) {
                return criteriaBuilder.between(root.get("montant"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("montant"), min);
            } else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("montant"), max);
            }
            return null;
        };
    }

    public static Specification<Paiement> echeanceAujourdhui() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dateEcheance"), LocalDate.now());
    }

    public static Specification<Paiement> echeanceCetteSemaine() {
        return (root, query, criteriaBuilder) -> {
            LocalDate aujourdhui = LocalDate.now();
            LocalDate debutSemaine = aujourdhui.minusDays(aujourdhui.getDayOfWeek().getValue() - 1);
            LocalDate finSemaine = debutSemaine.plusDays(6);
            return criteriaBuilder.between(root.get("dateEcheance"), debutSemaine, finSemaine);
        };
    }

    public static Specification<Paiement> echeanceCeMois() {
        return (root, query, criteriaBuilder) -> {
            LocalDate aujourdhui = LocalDate.now();
            LocalDate debutMois = aujourdhui.withDayOfMonth(1);
            LocalDate finMois = aujourdhui.withDayOfMonth(aujourdhui.lengthOfMonth());
            return criteriaBuilder.between(root.get("dateEcheance"), debutMois, finMois);
        };
    }

    public static Specification<Paiement> payeCetteSemaine() {
        return (root, query, criteriaBuilder) -> {
            LocalDate aujourdhui = LocalDate.now();
            LocalDate debutSemaine = aujourdhui.minusDays(aujourdhui.getDayOfWeek().getValue() - 1);
            LocalDate finSemaine = debutSemaine.plusDays(6);
            java.time.LocalDateTime debutSemaineTime = debutSemaine.atStartOfDay();
            java.time.LocalDateTime finSemaineTime = finSemaine.atTime(23, 59, 59);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("statut"), Paiement.Statut.PAYE),
                    criteriaBuilder.between(root.get("datePaiement"), debutSemaineTime, finSemaineTime)
            );
        };
    }

    public static Specification<Paiement> payeCeMois() {
        return (root, query, criteriaBuilder) -> {
            LocalDate aujourdhui = LocalDate.now();
            LocalDate debutMois = aujourdhui.withDayOfMonth(1);
            LocalDate finMois = aujourdhui.withDayOfMonth(aujourdhui.lengthOfMonth());
            java.time.LocalDateTime debutMoisTime = debutMois.atStartOfDay();
            java.time.LocalDateTime finMoisTime = finMois.atTime(23, 59, 59);

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("statut"), Paiement.Statut.PAYE),
                    criteriaBuilder.between(root.get("datePaiement"), debutMoisTime, finMoisTime)
            );
        };
    }

    // Spécifications pour jointures avec Contrat (si nécessaire)

    public static Specification<Paiement> contratActif() {
        return (root, query, criteriaBuilder) -> {
            var contratJoin = root.join("contrat");
            return criteriaBuilder.equal(contratJoin.get("statut"), "ACTIF"); // Assumant que Contrat a un enum Statut.ACTIF
        };
    }

    public static Specification<Paiement> contratLocataireId(Long locataireId) {
        return (root, query, criteriaBuilder) -> {
            if (locataireId != null) {
                var contratJoin = root.join("contrat");
                return criteriaBuilder.equal(contratJoin.get("locataireId"), locataireId);
            }
            return null;
        };
    }
}