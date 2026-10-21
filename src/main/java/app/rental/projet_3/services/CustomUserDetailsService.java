package app.rental.projet_3.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Appelée par Spring Security lors du login pour charger l'utilisateur et son mot de passe haché.
	 * Spring appelle l'identifiant "username" : dans notre application, c'est l'email.
	 *
	 * @param email l'email saisi au login
	 * @return l'utilisateur au format attendu par Spring Security
	 * @throws UsernameNotFoundException si aucun utilisateur n'a cet email
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return User.withUsername(user.getEmail())
				.password(user.getPassword())
				.build();
	}
}
