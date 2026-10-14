package app.rental.projet_3.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import app.rental.projet_3.entities.RentalEntity;

public class RentalDto {

	// Pas d'ownerId : le proprietaire est l'utilisateur du token
	public record RentalInput(
			String name,
			BigDecimal surface,
			BigDecimal price,
			String picture,
			String description) {
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
