package uadb.location.logement.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uadb.location.logement.model.Chambre.Chambre;
import uadb.location.logement.model.rendezVous.RendezVous;

import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends CrudRepository<RendezVous, Long>, JpaSpecificationExecutor<RendezVous> {

    void deleteByChambreIdIn(List<Long> chambreIds);

    @Modifying
    @Query("DELETE FROM RendezVous r WHERE r.chambre.id = :chambreId")
    void deleteByChambreId(@Param("chambreId") Long chambreId);

    @Query("SELECT r FROM RendezVous r " +
            "LEFT JOIN FETCH r.chambre c " +
            "LEFT JOIN FETCH c.maison m " +
            "LEFT JOIN FETCH c.medias " +
            "WHERE r.id = :rendezVousId")
    Optional<RendezVous> findByIdWithChambre(@Param("rendezVousId") Long rendezVousId);
}
