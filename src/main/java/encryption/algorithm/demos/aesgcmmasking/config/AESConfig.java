package encryption.algorithm.demos.aesgcmmasking.config;

import encryption.algorithm.demos.aesgcmmasking.encryption.AESGCM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AESConfig {
    @Bean
    public AESGCM dataEncoder() {
        return new AESGCM();
    }
}
