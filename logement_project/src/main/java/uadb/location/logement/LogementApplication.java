package uadb.location.logement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableDiscoveryClient
@SpringBootApplication
public class LogementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogementApplication.class, args);
	}

}
