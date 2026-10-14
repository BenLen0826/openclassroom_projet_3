package app.rental.projet_3.dto;

public class AuthDto {

	public record RegisterInput(String email, String name, String password) {
	}

	public record LoginInput(String email, String password) {
	}

	public record TokenOutput(String token) {
	}
}
