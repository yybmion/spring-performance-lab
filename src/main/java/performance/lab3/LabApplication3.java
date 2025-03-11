package performance.lab3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LabApplication3 {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-3");
        SpringApplication.run(LabApplication3.class, args);
    }
}
