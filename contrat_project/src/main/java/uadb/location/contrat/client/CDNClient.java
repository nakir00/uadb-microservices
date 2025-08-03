package uadb.location.contrat.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface CDNClient {

    @PostMapping(value = "/api/cdn/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void deleteMedia();
}
