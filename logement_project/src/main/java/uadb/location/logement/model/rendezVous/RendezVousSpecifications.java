package uadb.location.logement.model.rendezVous;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.maison.Maison;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousSpecifications {

    // Méthode principale pour combiner toutes les spécifications
    public static Specification<RendezVous> withCriteria(RendezVousSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Critères sur les champs directs de RendezVous
            if (criteria.getLocataireId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("locataireId"), criteria.getLocataireId()));
            }

            if (criteria.getStatut() != null) {
                predicates.add(criteriaBuilder.equal(root.get("statut"), criteria.getStatut()));
            }

            // Critères sur les heures de rendez-vous
            if (criteria.getDateHeureMin() != null || criteria.getDateHeureMax() != null) {
                if (criteria.getDateHeureMin() != null && criteria.getDateHeureMax() != null) {
                    predicates.add(criteriaBuilder.between(root.get("dateHeure"),
                            criteria.getDateHeureMin(), criteria.getDateHeureMax()));
                } else if (criteria.getDateHeureMin() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateHeure"),
                            criteria.getDateHeureMin()));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateHeure"),
                            criteria.getDateHeureMax()));
                }
            }

            // Critères sur les dates de création
            if (criteria.getCreeLeSince() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"),
                        criteria.getCreeLeSince()));
            }

            if (criteria.getCreeLeUntil() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creeLe"),
                        criteria.getCreeLeUntil()));
            }

            // ========== LOGIQUE POUR PROPRIÉTAIRE, MAISON ET CHAMBRE ==========
            // Si proprietaireId est fourni, on filtre par propriétaire
            if (criteria.getProprietaireId() != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                Join<Chambre, Maison> maisonJoin = chambreJoin.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(
                        maisonJoin.get("proprietaireId"),
                        criteria.getProprietaireId()
                ));
            }
            // Si proprietaireId n'est pas fourni mais maisonId l'est, on filtre par maison
            else if (criteria.getMaisonId() != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("maison").get("id"),
                        criteria.getMaisonId()));
            }
            // Si ni proprietaireId ni maisonId, mais chambreId est fourni
            else if (criteria.getChambreId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("chambre").get("id"),
                        criteria.getChambreId()));
            }

            // Critères sur les champs de la chambre
            if (criteria.getChambreTitre() != null && !criteria.getChambreTitre().trim().isEmpty()) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(chambreJoin.get("titre")),
                        "%" + criteria.getChambreTitre().toLowerCase() + "%"
                ));
            }

            if (criteria.getChambreDescription() != null && !criteria.getChambreDescription().trim().isEmpty()) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(chambreJoin.get("description")),
                        "%" + criteria.getChambreDescription().toLowerCase() + "%"
                ));
            }

            if (criteria.getChambreTaille() != null && !criteria.getChambreTaille().trim().isEmpty()) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(chambreJoin.get("taille")),
                        "%" + criteria.getChambreTaille().toLowerCase() + "%"
                ));
            }

            if (criteria.getChambreType() != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("type"), criteria.getChambreType()));
            }

            if (criteria.getChambreMeublee() != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("meublee"), criteria.getChambreMeublee()));
            }

            if (criteria.getChambreSalleDeBain() != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("salleDeBain"), criteria.getChambreSalleDeBain()));
            }

            if (criteria.getChambreDisponible() != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("disponible"), criteria.getChambreDisponible()));
            }

            // Critères sur les champs de la maison
            if (criteria.getMaisonNom() != null && !criteria.getMaisonNom().trim().isEmpty()) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                Join<Chambre, Maison> maisonJoin = chambreJoin.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(maisonJoin.get("nom")),
                        "%" + criteria.getMaisonNom().toLowerCase() + "%"
                ));
            }

            if (criteria.getMaisonVille() != null && !criteria.getMaisonVille().trim().isEmpty()) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                Join<Chambre, Maison> maisonJoin = chambreJoin.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(maisonJoin.get("ville")),
                        "%" + criteria.getMaisonVille().toLowerCase() + "%"
                ));
            }

            if (criteria.getMaisonQuartier() != null && !criteria.getMaisonQuartier().trim().isEmpty()) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                Join<Chambre, Maison> maisonJoin = chambreJoin.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(maisonJoin.get("quartier")),
                        "%" + criteria.getMaisonQuartier().toLowerCase() + "%"
                ));
            }

            // Critères utilitaires
            if (criteria.getHasDateHeure() != null) {
                if (criteria.getHasDateHeure()) {
                    predicates.add(criteriaBuilder.isNotNull(root.get("dateHeure")));
                } else {
                    predicates.add(criteriaBuilder.isNull(root.get("dateHeure")));
                }
            }

            if (criteria.getRecentDays() != null) {
                LocalDateTime cutoffDate = LocalDateTime.now().minusDays(criteria.getRecentDays());
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), cutoffDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Spécifications individuelles pour utilisation modulaire

    // Spécification pour le locataire
    public static Specification<RendezVous> hasLocataireId(Long locataireId) {
        return (root, query, criteriaBuilder) -> {
            if (locataireId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("locataireId"), locataireId);
        };
    }

    // Spécification pour le statut
    public static Specification<RendezVous> hasStatut(RendezVous.Statut statut) {
        return (root, query, criteriaBuilder) -> {
            if (statut == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("statut"), statut);
        };
    }

    // Spécification pour la plage d'heures
    public static Specification<RendezVous> hasDateHeureBetween(LocalDateTime dateHeureMin, LocalDateTime dateHeureMax) {
        return (root, query, criteriaBuilder) -> {
            if (dateHeureMin == null && dateHeureMax == null) {
                return criteriaBuilder.conjunction();
            }

            if (dateHeureMin != null && dateHeureMax != null) {
                return criteriaBuilder.between(root.get("dateHeure"), dateHeureMin, dateHeureMax);
            }

            if (dateHeureMin != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateHeure"), dateHeureMin);
            }

            return criteriaBuilder.lessThanOrEqualTo(root.get("dateHeure"), dateHeureMax);
        };
    }

    // Spécification pour la chambre
    public static Specification<RendezVous> hasChambreId(Long chambreId) {
        return (root, query, criteriaBuilder) -> {
            if (chambreId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("chambre").get("id"), chambreId);
        };
    }

    // Spécification pour le propriétaire (via chambre -> maison)
    public static Specification<RendezVous> hasProprietaireId(Long proprietaireId) {
        return (root, query, criteriaBuilder) -> {
            if (proprietaireId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
            Join<Chambre, Maison> maisonJoin = chambreJoin.join("maison", JoinType.LEFT);
            return criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId);
        };
    }

    // Spécification pour la maison (via chambre)
    public static Specification<RendezVous> hasMaisonId(Long maisonId) {
        return (root, query, criteriaBuilder) -> {
            if (maisonId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
            return criteriaBuilder.equal(chambreJoin.get("maison").get("id"), maisonId);
        };
    }

    // Spécification pour les dates de création
    public static Specification<RendezVous> isCreatedBetween(LocalDateTime since, LocalDateTime until) {
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

    // Spécification pour les rendez-vous récents
    public static Specification<RendezVous> isRecentlyCreated(int days) {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
            return criteriaBuilder.greaterThanOrEqualTo(root.get("creeLe"), cutoffDate);
        };
    }

    // Spécification pour les rendez-vous avec ou sans heure définie
    public static Specification<RendezVous> hasDateHeureSet(boolean hasDateHeure) {
        return (root, query, criteriaBuilder) -> {
            if (hasDateHeure) {
                return criteriaBuilder.isNotNull(root.get("dateHeure"));
            } else {
                return criteriaBuilder.isNull(root.get("dateHeure"));
            }
        };
    }

    // Spécification pour les rendez-vous d'un propriétaire avec des filtres
    public static Specification<RendezVous> hasProprietaireIdWithFilters(Long proprietaireId,
                                                                         RendezVous.Statut statut,
                                                                         Boolean chambreDisponible) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (proprietaireId != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                Join<Chambre, Maison> maisonJoin = chambreJoin.join("maison", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(maisonJoin.get("proprietaire").get("id"), proprietaireId));
            }

            if (statut != null) {
                predicates.add(criteriaBuilder.equal(root.get("statut"), statut));
            }

            if (chambreDisponible != null) {
                Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(chambreJoin.get("disponible"), chambreDisponible));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Spécification pour les rendez-vous avec informations de chambre chargées
    public static Specification<RendezVous> withChambreInfo() {
        return (root, query, criteriaBuilder) -> {
            root.fetch("chambre", JoinType.LEFT);
            return criteriaBuilder.conjunction();
        };
    }

    // Spécification pour les rendez-vous avec informations complètes (chambre + maison)
    public static Specification<RendezVous> withCompleteInfo() {
        return (root, query, criteriaBuilder) -> {
            Fetch<RendezVous, Chambre> chambreFetch = root.fetch("chambre", JoinType.LEFT);
            chambreFetch.fetch("maison", JoinType.LEFT);
            return criteriaBuilder.conjunction();
        };
    }

    // Spécification pour rechercher dans les informations de chambre
    public static Specification<RendezVous> searchInChambreInfo(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            Join<RendezVous, Chambre> chambreJoin = root.join("chambre", JoinType.LEFT);

            Predicate titreMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(chambreJoin.get("titre")),
                    searchPattern
            );

            Predicate descriptionMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(chambreJoin.get("description")),
                    searchPattern
            );

            return criteriaBuilder.or(titreMatch, descriptionMatch);
        };
    }

    // Spécification pour les rendez-vous d'aujourd'hui
    public static Specification<RendezVous> isToday() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);

            return criteriaBuilder.between(root.get("creeLe"), startOfDay, endOfDay);
        };
    }

    // Spécification pour les rendez-vous en attente
    public static Specification<RendezVous> isPending() {
        return hasStatut(RendezVous.Statut.EN_ATTENTE);
    }

    // Spécification pour les rendez-vous confirmés
    public static Specification<RendezVous> isConfirmed() {
        return hasStatut(RendezVous.Statut.CONFIRME);
    }

    // Spécification pour les rendez-vous annulés
    public static Specification<RendezVous> isCancelled() {
        return hasStatut(RendezVous.Statut.ANNULE);
    }
}