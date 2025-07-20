package uadb.location.logement.model.maison;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifications pour les requêtes JPA sur l'entité Maison
 */
public class MaisonSpecifications {

    /**
     * Filtre par ID du propriétaire
     */
    public static Specification<Maison> hasProprietaireId(Long proprietaireId) {
        return (root, query, criteriaBuilder) -> {
            if (proprietaireId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("proprietaireId"), proprietaireId);
        };
    }

    /**
     * Filtre par adresse (recherche partielle, insensible à la casse)
     */
    public static Specification<Maison> hasAdresse(String adresse) {
        return (root, query, criteriaBuilder) -> {
            if (adresse == null || adresse.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("adresse")),
                    "%" + adresse.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtre par description (recherche partielle, insensible à la casse)
     */
    public static Specification<Maison> hasDescription(String description) {
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

    /**
     * Filtre par zone géographique (rayon autour d'un point)
     *
     * @param centerLat Latitude du centre
     * @param centerLon Longitude du centre
     * @param radiusKm  Rayon en kilomètres
     */
    public static Specification<Maison> withinRadius(BigDecimal centerLat, BigDecimal centerLon, double radiusKm) {
        return (root, query, criteriaBuilder) -> {
            if (centerLat == null || centerLon == null) {
                return criteriaBuilder.conjunction();
            }

            // Formule de Haversine simplifiée pour calculer la distance
            // Note: Pour une précision parfaite, utiliser une fonction SQL native
            double latDiff = 111.0; // Approximation: 1 degré latitude ≈ 111 km
            double lonDiff = 111.0 * Math.cos(Math.toRadians(centerLat.doubleValue())); // Ajusté pour longitude

            return criteriaBuilder.and(
                    criteriaBuilder.between(root.get("latitude"),
                            centerLat.subtract(BigDecimal.valueOf(radiusKm / latDiff)),
                            centerLat.add(BigDecimal.valueOf(radiusKm / latDiff))),
                    criteriaBuilder.between(root.get("longitude"),
                            centerLon.subtract(BigDecimal.valueOf(radiusKm / lonDiff)),
                            centerLon.add(BigDecimal.valueOf(radiusKm / lonDiff)))
            );
        };
    }

    /**
     * Filtre par plage de latitude
     */
    public static Specification<Maison> hasLatitudeBetween(BigDecimal latMin, BigDecimal latMax) {
        return (root, query, criteriaBuilder) -> {
            if (latMin == null && latMax == null) {
                return criteriaBuilder.conjunction();
            }
            if (latMin != null && latMax != null) {
                return criteriaBuilder.between(root.get("latitude"), latMin, latMax);
            }
            if (latMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("latitude"), latMin);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("latitude"), latMax);
        };
    }

    /**
     * Filtre par plage de longitude
     */
    public static Specification<Maison> hasLongitudeBetween(BigDecimal lonMin, BigDecimal lonMax) {
        return (root, query, criteriaBuilder) -> {
            if (lonMin == null && lonMax == null) {
                return criteriaBuilder.conjunction();
            }
            if (lonMin != null && lonMax != null) {
                return criteriaBuilder.between(root.get("longitude"), lonMin, lonMax);
            }
            if (lonMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("longitude"), lonMin);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("longitude"), lonMax);
        };
    }

    /**
     * Filtre par date de création (maisons créées après une certaine date)
     */
    public static Specification<Maison> createdAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), date);
        };
    }

    /**
     * Filtre par date de création (maisons créées avant une certaine date)
     */
    public static Specification<Maison> createdBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), date);
        };
    }

    /**
     * Filtre par période de création
     */
    public static Specification<Maison> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("creeLe"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), endDate);
        };
    }

    /**
     * Filtre par nombre de chambres
     */
    public static Specification<Maison> hasNombreChambres(Integer nombreChambres) {
        return (root, query, criteriaBuilder) -> {
            if (nombreChambres == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.size(root.get("chambres")),
                    nombreChambres
            );
        };
    }

    /**
     * Filtre par nombre minimum de chambres
     */
    public static Specification<Maison> hasMinimumChambres(Integer minChambres) {
        return (root, query, criteriaBuilder) -> {
            if (minChambres == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(
                    criteriaBuilder.size(root.get("chambres")),
                    minChambres
            );
        };
    }

    /**
     * Filtre par chambres disponibles
     */
    public static Specification<Maison> hasChambresDisponibles(Boolean hasAvailableRooms) {
        return (root, query, criteriaBuilder) -> {
            if (hasAvailableRooms == null || !hasAvailableRooms) {
                return criteriaBuilder.conjunction();
            }

            Join<Maison, Object> chambreJoin = root.join("chambres", JoinType.INNER);
            return criteriaBuilder.equal(chambreJoin.get("disponible"), true);
        };
    }

    /**
     * Filtre par plage de prix des chambres
     */
    public static Specification<Maison> hasChambresPrixBetween(Double prixMin, Double prixMax) {
        return (root, query, criteriaBuilder) -> {
            if (prixMin == null && prixMax == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Maison, Object> chambreJoin = root.join("chambres", JoinType.INNER);

            if (prixMin != null && prixMax != null) {
                return criteriaBuilder.between(chambreJoin.get("prix"), prixMin, prixMax);
            }
            if (prixMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(chambreJoin.get("prix"), prixMin);
            }
            return criteriaBuilder.lessThanOrEqualTo(chambreJoin.get("prix"), prixMax);
        };
    }

    /**
     * Filtre par type de chambre disponible
     */
    public static Specification<Maison> hasChambresOfType(String typeChambre) {
        return (root, query, criteriaBuilder) -> {
            if (typeChambre == null || typeChambre.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Maison, Object> chambreJoin = root.join("chambres", JoinType.INNER);
            return criteriaBuilder.equal(chambreJoin.get("type"), typeChambre);
        };
    }

    /**
     * Specification complexe combinant plusieurs critères
     */
    public static Specification<Maison> withCriteria(MaisonSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getProprietaireId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("proprietaireId"), criteria.getProprietaireId()));
            }

            if (criteria.getAdresse() != null && !criteria.getAdresse().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("adresse")),
                        "%" + criteria.getAdresse().toLowerCase() + "%"
                ));
            }

            if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + criteria.getDescription().toLowerCase() + "%"
                ));
            }

            if (criteria.getLatMin() != null || criteria.getLatMax() != null) {
                if (criteria.getLatMin() != null && criteria.getLatMax() != null) {
                    predicates.add(criteriaBuilder.between(root.get("latitude"), criteria.getLatMin(), criteria.getLatMax()));
                } else if (criteria.getLatMin() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("latitude"), criteria.getLatMin()));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("latitude"), criteria.getLatMax()));
                }
            }

            if (criteria.getLonMin() != null || criteria.getLonMax() != null) {
                if (criteria.getLonMin() != null && criteria.getLonMax() != null) {
                    predicates.add(criteriaBuilder.between(root.get("longitude"), criteria.getLonMin(), criteria.getLonMax()));
                } else if (criteria.getLonMin() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("longitude"), criteria.getLonMin()));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("longitude"), criteria.getLonMax()));
                }
            }

            if (criteria.getDateCreationMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), criteria.getDateCreationMin()));
            }

            if (criteria.getDateCreationMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"), criteria.getDateCreationMax()));
            }

            if (criteria.getMinChambres() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.size(root.get("chambres")),
                        criteria.getMinChambres()
                ));
            }

            if (criteria.getHasChambresDisponibles() != null && criteria.getHasChambresDisponibles()) {
                Join<Maison, Object> chambreJoin = root.join("chambres", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("disponible"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}