package uadb.location.contrat;

import org.springframework.boot.SpringApplication;

public class TestContratApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContratApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
