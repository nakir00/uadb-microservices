package uadb.location.logement.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uadb.location.logement.model.maison.Maison;

import java.util.Optional;

public interface  MaisonRepository extends JpaRepository<Maison, Long>, JpaSpecificationExecutor<Maison> {

    Page<Maison> findByDescriptionContainingIgnoreCaseAndProprietaireId(String name, Long proprietaireId, Pageable pageable);

    Optional<Maison> findByIdAndProprietaireId(Long id,  Long proprietaireI);

    boolean existsByIdAndProprietaireId(Long id, Long proprietaireId);



}
