package app.rental.projet_3.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.rental.projet_3.entities.UserEntity;

public class UserDto {

	public record UserOutput(
			Integer id,
			String name,
			String email,
			@JsonProperty("created_at") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime createdAt,
			@JsonProperty("updated_at") @JsonFormat(pattern = "yyyy/MM/dd") LocalDateTime updatedAt) {

		public static UserOutput fromEntity(UserEntity user) {
			return new UserOutput(
					user.getId(),
					user.getName(),
					user.getEmail(),
					user.getCreatedAt(),
					user.getUpdatedAt());
		}
	}
}
