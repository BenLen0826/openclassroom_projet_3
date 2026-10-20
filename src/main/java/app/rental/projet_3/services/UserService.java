package app.rental.projet_3.services;

import org.springframework.stereotype.Service;

import app.rental.projet_3.dto.UserDto.UserOutput;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.exceptions.ResourceNotFoundException;
import app.rental.projet_3.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserOutput getUserById(Integer id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return UserOutput.fromEntity(user);
	}
}
