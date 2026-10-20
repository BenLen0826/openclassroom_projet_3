package app.rental.projet_3.exceptions;

public class NotResourceOwnerException extends RuntimeException {
	public NotResourceOwnerException(String message) {
		super(message);
	}
}
