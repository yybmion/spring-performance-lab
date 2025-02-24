package performance.lab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LabApplication2 {
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-2");
        SpringApplication.run(LabApplication2.class, args);
    }
}
