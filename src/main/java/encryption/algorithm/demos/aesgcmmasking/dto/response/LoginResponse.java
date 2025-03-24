package encryption.algorithm.demos.aesgcmmasking.dto.response;

import lombok.*;

@Builder
@Getter
public class LoginResponse {
    private String salt;
    private String key;
    private String iv;
    private String tag;
}
