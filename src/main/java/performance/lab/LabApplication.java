package performance.lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LabApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application");
		SpringApplication.run(LabApplication.class, args);
	}

}
