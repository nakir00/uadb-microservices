package uadb.location.logement.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import uadb.location.logement.dto.client.UtilisateurClient.ReadUtilisateurDTO;

public interface UtilisateurClient {

    @GetExchange("/api/user/{id}")
    ReadUtilisateurDTO getUserById(@PathVariable Long Id);

}
