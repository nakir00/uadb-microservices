package uadb.location.logement.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uadb.location.logement.dto.client.mediaClient.CreateMediaDto;

public interface CDNClient {

    @PostMapping("/api/cdn")
    CreateMediaDto createMedia(@RequestParam("files") MultipartFile[] files);
}
