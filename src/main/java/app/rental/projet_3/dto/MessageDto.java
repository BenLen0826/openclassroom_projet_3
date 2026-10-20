package app.rental.projet_3.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MessageDto {

	public record MessageInput(
			@NotBlank @Size(max = 2000) String message,
			@NotNull @JsonProperty("user_id") Integer userId,
			@NotNull @JsonProperty("rental_id") Integer rentalId) {
	}
}
