package uadb.location.contrat.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import uadb.location.contrat.dto.client.UtilisateurClient.ReadUtilisateurDTO;

public interface UtilisateurClient {

    @GetExchange("/api/user/{id}")
    ReadUtilisateurDTO getUserById(@PathVariable Long Id);

}
