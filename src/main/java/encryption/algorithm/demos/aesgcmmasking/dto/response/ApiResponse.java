package encryption.algorithm.demos.aesgcmmasking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
   private String message;
   private T data;
}
