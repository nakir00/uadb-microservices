package uadb.location.contrat.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import uadb.location.contrat.model.probleme.Probleme;

public interface ProblemeRepository  extends CrudRepository<Probleme, Long>, JpaSpecificationExecutor<Probleme> {

}
