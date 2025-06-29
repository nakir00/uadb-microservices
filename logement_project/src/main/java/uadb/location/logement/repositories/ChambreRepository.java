package uadb.location.logement.repositories;

import org.springframework.data.repository.CrudRepository;
import uadb.location.logement.model.Chambre;

public interface ChambreRepository extends CrudRepository<Chambre, Long> {
}
