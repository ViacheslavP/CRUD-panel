package boot.viapivov.crudboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CrudBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudBootApplication.class, args);
    }
}
