package app.rental.projet_3.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import app.rental.projet_3.entities.RentalEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RentalDto {

	// Pas d'ownerId : le proprietaire est l'utilisateur du token
	public record RentalInput(
			@NotBlank String name,
			@NotNull @Positive BigDecimal surface,
			@NotNull @Positive BigDecimal price,
			@NotNull MultipartFile picture,
			@Size(max = 2000) String description) {
	}

	public record RentalOutput(
			Integer id,
			String name,
			BigDecimal surface,
			BigDecimal price,
			String picture,
			String description,
			@JsonProperty("owner_id") Integer ownerId,
			@JsonProperty("created_at") LocalDateTime createdAt,
			@JsonProperty("updated_at") LocalDateTime updatedAt) {

		public static RentalOutput fromEntity(RentalEntity rental) {
			return new RentalOutput(
					rental.getId(),
					rental.getName(),
					rental.getSurface(),
					rental.getPrice(),
					rental.getPicture(),
					rental.getDescription(),
					rental.getOwnerId(),
					rental.getCreatedAt(),
					rental.getUpdatedAt());
		}
	}
}
