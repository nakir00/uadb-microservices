package uadb.location.logement.client;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.HttpExchange;
import uadb.location.logement.dto.client.mediaClient.CreateMediaDto;

import java.util.List;

@HttpExchange
public interface CDNClient {

    @PostMapping(value = "/api/cdn/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void deleteMedia();
}
