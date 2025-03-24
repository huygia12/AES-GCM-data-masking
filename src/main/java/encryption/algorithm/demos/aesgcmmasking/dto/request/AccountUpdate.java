package encryption.algorithm.demos.aesgcmmasking.dto.request;

import encryption.algorithm.demos.aesgcmmasking.constant.StatusMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdate {
    static final String EMPTY_CHECKING_PATTERN = "^(?!\s*$).+";

    @Schema(example = "abc@gmail.com")
    @Email(message = StatusMessage.EMAIL_INVALID)
    @Pattern(regexp = EMPTY_CHECKING_PATTERN, message = "email is mandatory")
    private String email;

    @Schema(example = "Nguyen van A")
    private String name;

    private String address;

    private String phone;

    @Schema(example = "2004-07-14")
    @Past(message = StatusMessage.BIRTH_DATE_INVALID)
    private LocalDate dateOfBirth;

    private String citizenID;
}
