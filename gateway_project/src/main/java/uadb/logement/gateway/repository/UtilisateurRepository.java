package uadb.logement.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uadb.logement.gateway.model.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository  extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);
}
