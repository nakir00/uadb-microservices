package uadb.location.cdn_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class CdnProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CdnProjectApplication.class, args);
	}

}
