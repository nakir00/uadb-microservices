package uadb.location.logement;

import org.springframework.boot.SpringApplication;

public class TestLogementApplication {

	public static void main(String[] args) {
		SpringApplication.from(LogementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
