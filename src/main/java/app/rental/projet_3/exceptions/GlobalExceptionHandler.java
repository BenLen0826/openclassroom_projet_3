package app.rental.projet_3.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
		// Seule la première erreur de validation est renvoyée
		String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleNotFound(ResourceNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ProblemDetail handleInvalidCredentials(InvalidCredentialsException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ProblemDetail handleAlreadyExists(ResourceAlreadyExistsException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
	}

	@ExceptionHandler(InvalidFileException.class)
	public ProblemDetail handleInvalidFile(InvalidFileException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleOther(Exception e) {
		log.error("Erreur inattendue", e);
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
	}
}
