package uadb.location.logement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uadb.location.logement.model.Media;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

     void deleteByChambreIdIn(List<Long> chambreIds);

    @Modifying
    @Query("DELETE FROM Media m WHERE m.chambre.id = :chambreId")
    void deleteByChambreId(@Param("chambreId") Long chambreId);

    @Modifying
    @Query("DELETE FROM Media m WHERE m.id = :mediaId")
    void deleteById(@Param("mediaId") Long mediaId);
}
