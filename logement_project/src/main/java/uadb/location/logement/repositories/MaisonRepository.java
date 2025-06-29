package uadb.location.logement.repositories;

import org.springframework.data.repository.CrudRepository;
import uadb.location.logement.model.Chambre;
import uadb.location.logement.model.Maison;

public interface  MaisonRepository extends CrudRepository<Maison, Long> {

}
