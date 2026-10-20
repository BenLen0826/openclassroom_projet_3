package app.rental.projet_3.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.rental.projet_3.entities.RentalEntity;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RentalDto {

	// Pas d'ownerId : le proprietaire est l'utilisateur du token
	public record RentalInput(
			@NotBlank @Size(max = 255) String name,
			@NotNull @Positive @Digits(integer = 10, fraction = 0) BigDecimal surface,
			@NotNull @Positive @Digits(integer = 10, fraction = 0) BigDecimal price,
			@NotNull MultipartFile picture,
			@Size(max = 2000) String description) {
	}

	public record RentalUpdateInput(
			@NotBlank @Size(max = 255) String name,
			@NotNull @Positive @Digits(integer = 10, fraction = 0) BigDecimal surface,
			@NotNull @Positive @Digits(integer = 10, fraction = 0) BigDecimal price,
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
			@JsonProperty("created_at") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime createdAt,
			@JsonProperty("updated_at") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime updatedAt) {

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

	public record RentalListOutput(List<RentalOutput> rentals) {
	}
}
