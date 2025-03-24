package encryption.algorithm.demos.aesgcmmasking.config;

import encryption.algorithm.demos.aesgcmmasking.util.AccountDTOMapper;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    private final Logger logger;

    public AppConfig() {
        this.logger = LogManager.getLogger(AppConfig.class);
    }

    @Bean
    public AccountDTOMapper accountDTOMapper() {
        return new AccountDTOMapper();
    }

}
