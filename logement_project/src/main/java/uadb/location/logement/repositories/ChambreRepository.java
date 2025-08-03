package uadb.location.logement.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.maison.Maison;

import java.util.List;
import java.util.Optional;

public interface ChambreRepository extends CrudRepository<Chambre, Long>, JpaSpecificationExecutor<Chambre> {
    List<Chambre> findByMaisonId(Long maisonId);


    Page<Chambre> findByTitreContainingIgnoreCaseAndMaisonId(String titre, Long maisonId, Pageable pageable);
    List<Chambre> findByTitreContainingIgnoreCaseAndMaisonId(String titre, Long maisonId);

    boolean existsByIdAndMaison_ProprietaireId(Long chambreId, Long proprietaireId);

    @Query("SELECT c FROM Chambre c JOIN FETCH c.maison WHERE c.id = :id")
    Optional<Chambre> findByIdWithMaison(@Param("id") Long id);


    Page<Chambre> findAll( Pageable pageable);

    Page<Chambre> findByTitreContainingIgnoreCase(String name, Pageable pageable);

    Page<Chambre> findByMaisonId(Long maisonId, Pageable pageable);


}
