package uadb.location.contrat.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import uadb.location.contrat.model.paiement.Paiement;

public interface PaiementRepository  extends CrudRepository<Paiement, Long>, JpaSpecificationExecutor<Paiement> {
}
