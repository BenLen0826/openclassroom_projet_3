package app.rental.projet_3.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

	public record RegisterInput(
			@NotBlank @Email @Size(max = 255) String email,
			@NotBlank @Size(max = 255) String name,
			// BCrypt refuse de hacher plus de 72 octets
			@NotBlank @Size(max = 72) String password) {
	}

	public record LoginInput(
			@NotBlank @Email String email,
			@NotBlank String password) {
	}

	public record TokenOutput(String token) {
	}
}
