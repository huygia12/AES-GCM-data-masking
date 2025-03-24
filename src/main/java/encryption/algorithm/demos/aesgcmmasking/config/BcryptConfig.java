package encryption.algorithm.demos.aesgcmmasking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BcryptConfig {
    private final int bcryptStrength;

    public BcryptConfig() {
        this.bcryptStrength = System.getenv("BCRYPT_STRENGTH") != null
                ? Integer.parseInt(System.getenv("BCRYPT_STRENGTH")) : 12;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(this.bcryptStrength);
    }
}
