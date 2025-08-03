package uadb.location.contrat.model.contrat;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import uadb.location.contrat.model.contrat.Contrat;

import java.util.ArrayList;
import java.util.List;

public class ContratSpecification {

    public static Specification<Contrat> withCriteria(ContratSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Recherche par ID exact
            if (criteria.id() != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), criteria.id()));
            }

            // Recherche par locataire ID
            if (criteria.locataireId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("locataireId"), criteria.locataireId()));
            }

            // Recherche par chambre ID
            if (criteria.chambreId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("chambreId"), criteria.chambreId()));
            }

            if (criteria.chambreIds() != null && !criteria.chambreIds().isEmpty()) {
                predicates.add(root.get("chambreId").in(criteria.chambreIds()));
            }

            // Recherche par date de début exacte
            if (criteria.dateDebut() != null) {
                predicates.add(criteriaBuilder.equal(root.get("dateDebut"), criteria.dateDebut()));
            }

            // Recherche par plage de dates de début
            if (criteria.dateDebutFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateDebut"), criteria.dateDebutFrom()));
            }
            if (criteria.dateDebutTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateDebut"), criteria.dateDebutTo()));
            }

            // Recherche par date de fin exacte
            if (criteria.dateFin() != null) {
                predicates.add(criteriaBuilder.equal(root.get("dateFin"), criteria.dateFin()));
            }

            // Recherche par plage de dates de fin
            if (criteria.dateFinFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateFin"), criteria.dateFinFrom()));
            }
            if (criteria.dateFinTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateFin"), criteria.dateFinTo()));
            }

            // Recherche par montant de caution exact
            if (criteria.montantCaution() != null) {
                predicates.add(criteriaBuilder.equal(root.get("montantCaution"), criteria.montantCaution()));
            }

            // Recherche par plage de montant de caution
            if (criteria.montantCautionMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("montantCaution"), criteria.montantCautionMin()));
            }
            if (criteria.montantCautionMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("montantCaution"), criteria.montantCautionMax()));
            }

            // Recherche par nombre de mois de caution exact
            if (criteria.moisCaution() != null) {
                predicates.add(criteriaBuilder.equal(root.get("moisCaution"), criteria.moisCaution()));
            }

            // Recherche par plage de mois de caution
            if (criteria.moisCautionMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("moisCaution"), criteria.moisCautionMin()));
            }
            if (criteria.moisCautionMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("moisCaution"), criteria.moisCautionMax()));
            }

            // Recherche par description (contient le texte)
            if (criteria.description() != null && !criteria.description().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + criteria.description().toLowerCase() + "%"
                ));
            }

            // Recherche par mode de paiement unique
            if (criteria.modePaiement() != null && !criteria.modePaiement().trim().isEmpty()) {
                try {
                    Contrat.ModePaiement modePaiement = Contrat.ModePaiement.valueOf(criteria.modePaiement().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("modePaiement"), modePaiement));
                } catch (IllegalArgumentException ignored) {
                    // Mode de paiement invalide, on ignore ce critère
                }
            }

            // Recherche par liste de modes de paiement
            if (criteria.modesPaiement() != null && !criteria.modesPaiement().isEmpty()) {
                List<Contrat.ModePaiement> validModes = new ArrayList<>();
                for (String mode : criteria.modesPaiement()) {
                    try {
                        validModes.add(Contrat.ModePaiement.valueOf(mode.toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                        // Mode invalide, on l'ignore
                    }
                }
                if (!validModes.isEmpty()) {
                    predicates.add(root.get("modePaiement").in(validModes));
                }
            }

            // Recherche par périodicité unique
            if (criteria.periodicite() != null && !criteria.periodicite().trim().isEmpty()) {
                try {
                    Contrat.Periodicite periodicite = Contrat.Periodicite.valueOf(criteria.periodicite().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("periodicite"), periodicite));
                } catch (IllegalArgumentException ignored) {
                    // Périodicité invalide, on ignore ce critère
                }
            }

            // Recherche par liste de périodicités
            if (criteria.periodicites() != null && !criteria.periodicites().isEmpty()) {
                List<Contrat.Periodicite> validPeriodicites = new ArrayList<>();
                for (String periodicite : criteria.periodicites()) {
                    try {
                        validPeriodicites.add(Contrat.Periodicite.valueOf(periodicite.toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                        // Périodicité invalide, on l'ignore
                    }
                }
                if (!validPeriodicites.isEmpty()) {
                    predicates.add(root.get("periodicite").in(validPeriodicites));
                }
            }

            // Recherche par statut unique
            if (criteria.statut() != null && !criteria.statut().trim().isEmpty()) {
                try {
                    Contrat.Statut statut = Contrat.Statut.valueOf(criteria.statut().toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("statut"), statut));
                } catch (IllegalArgumentException ignored) {
                    // Statut invalide, on ignore ce critère
                }
            }

            // Recherche par liste de statuts
            if (criteria.statuts() != null && !criteria.statuts().isEmpty()) {
                List<Contrat.Statut> validStatuts = new ArrayList<>();
                for (String statut : criteria.statuts()) {
                    try {
                        validStatuts.add(Contrat.Statut.valueOf(statut.toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                        // Statut invalide, on l'ignore
                    }
                }
                if (!validStatuts.isEmpty()) {
                    predicates.add(root.get("statut").in(validStatuts));
                }
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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Méthodes utilitaires pour des spécifications spécifiques

    public static Specification<Contrat> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
                id != null ? criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<Contrat> hasLocataireId(Long locataireId) {
        return (root, query, criteriaBuilder) ->
                locataireId != null ? criteriaBuilder.equal(root.get("locataireId"), locataireId) : null;
    }

    public static Specification<Contrat> hasChambreId(Long chambreId) {
        return (root, query, criteriaBuilder) ->
                chambreId != null ? criteriaBuilder.equal(root.get("chambreId"), chambreId) : null;
    }

    public static Specification<Contrat> hasStatut(Contrat.Statut statut) {
        return (root, query, criteriaBuilder) ->
                statut != null ? criteriaBuilder.equal(root.get("statut"), statut) : null;
    }

    public static Specification<Contrat> hasModePaiement(Contrat.ModePaiement modePaiement) {
        return (root, query, criteriaBuilder) ->
                modePaiement != null ? criteriaBuilder.equal(root.get("modePaiement"), modePaiement) : null;
    }

    public static Specification<Contrat> hasPeriodicite(Contrat.Periodicite periodicite) {
        return (root, query, criteriaBuilder) ->
                periodicite != null ? criteriaBuilder.equal(root.get("periodicite"), periodicite) : null;
    }

    public static Specification<Contrat> dateDebutBetween(java.time.LocalDate from, java.time.LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get("dateDebut"), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateDebut"), from);
            } else if (to != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dateDebut"), to);
            }
            return null;
        };
    }

    public static Specification<Contrat> dateFinBetween(java.time.LocalDate from, java.time.LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (from != null && to != null) {
                return criteriaBuilder.between(root.get("dateFin"), from, to);
            } else if (from != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateFin"), from);
            } else if (to != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dateFin"), to);
            }
            return null;
        };
    }

    public static Specification<Contrat> montantCautionBetween(java.math.BigDecimal min, java.math.BigDecimal max) {
        return (root, query, criteriaBuilder) -> {
            if (min != null && max != null) {
                return criteriaBuilder.between(root.get("montantCaution"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("montantCaution"), min);
            } else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("montantCaution"), max);
            }
            return null;
        };
    }

    public static Specification<Contrat> descriptionContains(String description) {
        return (root, query, criteriaBuilder) ->
                description != null && !description.trim().isEmpty() ?
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                                "%" + description.toLowerCase() + "%") : null;
    }

    public static Specification<Contrat> hasChambreIds(List<Long> chambreIds) {
        return (root, query, criteriaBuilder) ->
                chambreIds != null && !chambreIds.isEmpty() ? root.get("chambreId").in(chambreIds) : null;
    }
}
