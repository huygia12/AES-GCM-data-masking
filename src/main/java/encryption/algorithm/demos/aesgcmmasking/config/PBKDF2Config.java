package encryption.algorithm.demos.aesgcmmasking.config;

import encryption.algorithm.demos.aesgcmmasking.exception.encryption.GenerateKeyFromPWFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

@Configuration
public class PBKDF2Config {

    private static final int ITERATIONS = 100_000;
    private static final int KEY_SIZE = 128;
    private static final int SALT_SIZE = 16;  // Salt 128-bit

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    public PBKDF2Util pbkdf2Util(SecureRandom secureRandom) {
        return new PBKDF2Util(secureRandom);
    }

    @RequiredArgsConstructor
    public static class PBKDF2Util {

        private final SecureRandom secureRandom;

        public byte[] generateSalt() {
            byte[] salt = new byte[SALT_SIZE];
            secureRandom.nextBytes(salt);
            return salt;
        }

        public byte[] generateKeyFromPassword(String password, byte[] salt) throws GenerateKeyFromPWFailException {
            try{
                PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_SIZE);
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                return factory.generateSecret(spec).getEncoded();
            } catch (Exception e) {
                throw new GenerateKeyFromPWFailException("Generate key fail");
            }
        }
    }
}