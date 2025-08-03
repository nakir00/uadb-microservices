package uadb.location.contrat.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import uadb.location.contrat.model.contrat.Contrat;

public interface ContratRepository  extends CrudRepository<Contrat, Long>, JpaSpecificationExecutor<Contrat> {
}
