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

	/**
	 * Récupère un utilisateur à partir de son id.
	 *
	 * @param id l'id de l'utilisateur
	 * @return l'utilisateur, sans son mot de passe
	 * @throws ResourceNotFoundException si l'utilisateur n'existe pas (404)
	 */
	public UserOutput getUserById(Integer id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return UserOutput.fromEntity(user);
	}
}
