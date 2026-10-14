package app.rental.projet_3.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDto {

	public record RegisterInput(
			@NotBlank @Email String email,
			@NotBlank String name,
			@NotBlank String password) {
	}

	public record LoginInput(
			@NotBlank @Email String email,
			@NotBlank String password) {
	}

	public record TokenOutput(String token) {
	}
}
