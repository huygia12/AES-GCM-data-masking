package encryption.algorithm.demos.aesgcmmasking.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import encryption.algorithm.demos.aesgcmmasking.constant.StatusMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSignup {
    static final String EMPTY_CHECKING_PATTERN = "^(?!\s*$).+";

    @Schema(example = "abc@gmail.com")
    @Email(message = StatusMessage.EMAIL_INVALID)
    @NotNull
    @Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "email is mandatory")
    private String email;

    @Schema(example = "123456")
    @NotNull
    @Min(value=6, message = StatusMessage.PASSWORD_MINIMUM)
    @Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "password is mandatory")
    private String password;

    @Schema(example = "Nguyen van A")
    private String name;
}
