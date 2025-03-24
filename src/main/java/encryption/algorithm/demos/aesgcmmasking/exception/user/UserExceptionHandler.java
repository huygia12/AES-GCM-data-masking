package encryption.algorithm.demos.aesgcmmasking.exception.user;

import encryption.algorithm.demos.aesgcmmasking.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorResponse> handleException(UserNotFoundException exception) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()));
	}

	@ExceptionHandler(UserInvalidException.class)
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	public ResponseEntity<ErrorResponse> handleException(UserInvalidException exception) {
		return ResponseEntity.unprocessableEntity()
			.body(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()));
	}

	@ExceptionHandler(UserExistedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorResponse> handleException(UserExistedException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ErrorResponse(exception.getMessage(), System.currentTimeMillis()));
	}
}
