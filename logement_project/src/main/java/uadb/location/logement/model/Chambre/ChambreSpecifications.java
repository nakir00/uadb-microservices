package uadb.location.logement.model.Chambre;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import uadb.location.logement.model.maison.Maison;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChambreSpecifications {

    // Méthode principale pour combiner toutes les spécifications
    public static Specification<Chambre> withCriteria(ChambreSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Critères sur les champs directs de Chambre
            if (criteria.getTitre() != null && !criteria.getTitre().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("titre")),
                        "%" + criteria.getTitre().toLowerCase() + "%"
                ));
            }

            if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + criteria.getDescription().toLowerCase() + "%"
                ));
            }

            if (criteria.getTaille() != null && !criteria.getTaille().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("taille")),
                        "%" + criteria.getTaille().toLowerCase() + "%"
                ));
            }

            if (criteria.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), criteria.getType()));
            }

            if (criteria.getMeublee() != null) {
                predicates.add(criteriaBuilder.equal(root.get("meublee"), criteria.getMeublee()));
            }

            if (criteria.getSalleDeBain() != null) {
                predicates.add(criteriaBuilder.equal(root.get("salleDeBain"), criteria.getSalleDeBain()));
            }

            if (criteria.getDisponible() != null) {
                predicates.add(criteriaBuilder.equal(root.get("disponible"), criteria.getDisponible()));
            }

            // Critères sur les prix
            if (criteria.getPrixMin() != null || criteria.getPrixMax() != null) {
                if (criteria.getPrixMin() != null && criteria.getPrixMax() != null) {
                    predicates.add(criteriaBuilder.between(root.get("prix"), criteria.getPrixMin(), criteria.getPrixMax()));
                } else if (criteria.getPrixMin() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), criteria.getPrixMin()));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prix"), criteria.getPrixMax()));
                }
            }

            // Critères sur les dates de création
            if (criteria.getCreeLeSince() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), criteria.getCreeLeSince()));
            }

            if (criteria.getCreeLeUntil() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), criteria.getCreeLeUntil()));
            }

            // ========== LOGIQUE POUR PROPRIÉTAIRE ET MAISON ==========
            // Si proprietaireId est fourni, on filtre par propriétaire
            if (criteria.proprietaireId() != null) {
                Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(
                        maisonJoin.get("proprietaireId"),
                        criteria.proprietaireId()
                ));
            }
            // Si proprietaireId n'est pas fourni mais maisonId l'est, on filtre par maison
            else if (criteria.getMaisonId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("maison").get("id"), criteria.getMaisonId()));
            }

            // Critères sur les autres champs de la maison (indépendants du propriétaire)
            if (criteria.getMaisonNom() != null && !criteria.getMaisonNom().trim().isEmpty()) {
                Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(maisonJoin.get("nom")),
                        "%" + criteria.getMaisonNom().toLowerCase() + "%"
                ));
            }

            if (criteria.getMaisonVille() != null && !criteria.getMaisonVille().trim().isEmpty()) {
                Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(maisonJoin.get("ville")),
                        "%" + criteria.getMaisonVille().toLowerCase() + "%"
                ));
            }

            if (criteria.getMaisonQuartier() != null && !criteria.getMaisonQuartier().trim().isEmpty()) {
                Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(maisonJoin.get("quartier")),
                        "%" + criteria.getMaisonQuartier().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Nouvelle spécification pour le propriétaire
    public static Specification<Chambre> hasProprietaireId(Long proprietaireId) {
        return (root, query, criteriaBuilder) -> {
            if (proprietaireId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
            return criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId);
        };
    }

    // Spécification pour récupérer toutes les chambres d'un propriétaire avec filtres optionnels
    public static Specification<Chambre> hasProprietaireIdWithFilters(Long proprietaireId, Boolean disponible) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (proprietaireId != null) {
                Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId));
            }

            if (disponible != null) {
                predicates.add(criteriaBuilder.equal(root.get("disponible"), disponible));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Spécification pour récupérer les chambres par propriétaire avec statistiques
    public static Specification<Chambre> hasProprietaireIdGroupedByMaison(Long proprietaireId) {
        return (root, query, criteriaBuilder) -> {
            if (proprietaireId == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);

            // Grouper par maison pour des statistiques
            query.groupBy(maisonJoin.get("id"));

            return criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId);
        };
    }

    // Spécification pour compter les chambres par propriétaire
    /*public static Specification<Chambre> countByProprietaire(Long proprietaireId) {
        return (root, query, criteriaBuilder) -> {
            if (proprietaireId == null) {
                return criteriaBuilder.conjunction();
            }
            criteriaBuilder.count()
            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);

            // Pour les requêtes de comptage
            query.select(criteriaBuilder.count(root));

            return criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId);
        };
    }*/

    // Spécification pour récupérer les chambres d'un propriétaire avec informations de maison
    public static Specification<Chambre> hasProprietaireIdWithMaisonInfo(Long proprietaireId) {
        return (root, query, criteriaBuilder) -> {
            if (proprietaireId == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);

            // Assurer que les informations de maison sont chargées
            root.fetch("maison", JoinType.LEFT);

            return criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId);
        };
    }

    // Toutes les autres spécifications existantes restent inchangées...

    // Spécification pour le titre (recherche partielle, insensible à la casse)
    public static Specification<Chambre> hasTitre(String titre) {
        return (root, query, criteriaBuilder) -> {
            if (titre == null || titre.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("titre")),
                    "%" + titre.toLowerCase() + "%"
            );
        };
    }

    // Spécification pour la description (recherche partielle, insensible à la casse)
    public static Specification<Chambre> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if (description == null || description.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"
            );
        };
    }

    // Spécification pour la taille (recherche exacte ou partielle)
    public static Specification<Chambre> hasTaille(String taille) {
        return (root, query, criteriaBuilder) -> {
            if (taille == null || taille.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("taille")),
                    "%" + taille.toLowerCase() + "%"
            );
        };
    }

    // Spécification pour le type de chambre
    public static Specification<Chambre> hasType(Chambre.Type type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    // Spécification pour les chambres meublées
    public static Specification<Chambre> isMeublee(Boolean meublee) {
        return (root, query, criteriaBuilder) -> {
            if (meublee == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("meublee"), meublee);
        };
    }

    // Spécification pour les chambres avec salle de bain
    public static Specification<Chambre> hasSalleDeBain(Boolean salleDeBain) {
        return (root, query, criteriaBuilder) -> {
            if (salleDeBain == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("salleDeBain"), salleDeBain);
        };
    }

    // Spécification pour la disponibilité
    public static Specification<Chambre> isDisponible(Boolean disponible) {
        return (root, query, criteriaBuilder) -> {
            if (disponible == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("disponible"), disponible);
        };
    }

    // Spécification pour la plage de prix
    public static Specification<Chambre> hasPrixBetween(BigDecimal prixMin, BigDecimal prixMax) {
        return (root, query, criteriaBuilder) -> {
            if (prixMin == null && prixMax == null) {
                return criteriaBuilder.conjunction();
            }

            if (prixMin != null && prixMax != null) {
                return criteriaBuilder.between(root.get("prix"), prixMin, prixMax);
            }

            if (prixMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), prixMin);
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("prix"), prixMax);
        };
    }

    // Spécification pour la date de création
    public static Specification<Chambre> isCreatedBetween(LocalDateTime since, LocalDateTime until) {
        return (root, query, criteriaBuilder) -> {
            if (since == null && until == null) {
                return criteriaBuilder.conjunction();
            }

            if (since != null && until != null) {
                return criteriaBuilder.between(root.get("creeLe"), since, until);
            }

            if (since != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), since);
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), until);
        };
    }

    // Spécification pour l'ID de la maison
    public static Specification<Chambre> hasMaisonId(Long maisonId) {
        return (root, query, criteriaBuilder) -> {
            if (maisonId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("maison").get("id"), maisonId);
        };
    }

    // Spécification pour le nom de la maison
    public static Specification<Chambre> hasMaisonNom(String maisonNom) {
        return (root, query, criteriaBuilder) -> {
            if (maisonNom == null || maisonNom.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(maisonJoin.get("nom")),
                    "%" + maisonNom.toLowerCase() + "%"
            );
        };
    }

    // Spécification pour la ville de la maison
    public static Specification<Chambre> hasMaisonVille(String ville) {
        return (root, query, criteriaBuilder) -> {
            if (ville == null || ville.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(maisonJoin.get("ville")),
                    "%" + ville.toLowerCase() + "%"
            );
        };
    }

    // Spécification pour le quartier de la maison
    public static Specification<Chambre> hasMaisonQuartier(String quartier) {
        return (root, query, criteriaBuilder) -> {
            if (quartier == null || quartier.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Chambre, Maison> maisonJoin = root.join("maison", JoinType.LEFT);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(maisonJoin.get("quartier")),
                    "%" + quartier.toLowerCase() + "%"
            );
        };
    }

    // Spécifications utilitaires supplémentaires

    // Chambres avec médias
    public static Specification<Chambre> hasMedias() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotEmpty(root.get("medias"));
        };
    }

    // Chambres sans médias
    public static Specification<Chambre> hasNoMedias() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isEmpty(root.get("medias"));
        };
    }

    // Chambres avec rendez-vous
    public static Specification<Chambre> hasRendezVous() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.isNotEmpty(root.get("rendezVousList"));
        };
    }

    // Chambres récentes (derniers N jours)
    public static Specification<Chambre> isRecentlyCreated(int days) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), cutoffDate);
        };
    }

    // Chambres dans une plage de prix avec pourcentage de tolérance
    public static Specification<Chambre> hasPrixAroundValue(BigDecimal targetPrice, double tolerancePercent) {
        return (root, query, criteriaBuilder) -> {
            if (targetPrice == null) {
                return criteriaBuilder.conjunction();
            }

            double tolerance = targetPrice.doubleValue() * (tolerancePercent / 100.0);
            BigDecimal minPrice = targetPrice.subtract(BigDecimal.valueOf(tolerance));
            BigDecimal maxPrice = targetPrice.add(BigDecimal.valueOf(tolerance));

            return criteriaBuilder.between(root.get("prix"), minPrice, maxPrice);
        };
    }

    // Spécification pour recherche full-text (titre et description)
    public static Specification<Chambre> searchInTitreAndDescription(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + searchTerm.toLowerCase() + "%";

            Predicate titreMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("titre")),
                    searchPattern
            );

            Predicate descriptionMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    searchPattern
            );

            return criteriaBuilder.or(titreMatch, descriptionMatch);
        };
    }
}