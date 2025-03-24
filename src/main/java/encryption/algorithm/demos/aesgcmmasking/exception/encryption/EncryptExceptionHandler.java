package encryption.algorithm.demos.aesgcmmasking.exception.encryption;

import encryption.algorithm.demos.aesgcmmasking.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EncryptExceptionHandler {

	@ExceptionHandler(WrongCipherModeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleException(WrongCipherModeException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()));
	}

	@ExceptionHandler(GenerateKeyFromPWFailException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponse> handleException(GenerateKeyFromPWFailException exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()));
	}
}
