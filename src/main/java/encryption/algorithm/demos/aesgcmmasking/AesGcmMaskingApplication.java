package encryption.algorithm.demos.aesgcmmasking;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AesGcmMaskingApplication {
    /**
    private final Dotenv dotenv;

    public AesGcmMaskingApplication(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @PostConstruct
    public void init() {
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }**/
    public static void main(String[] args) {
        SpringApplication.run(AesGcmMaskingApplication.class, args);
    }

}
