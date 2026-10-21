package app.rental.projet_3.services;

import java.nio.charset.StandardCharsets;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.rental.projet_3.dto.AuthDto.LoginInput;
import app.rental.projet_3.dto.AuthDto.RegisterInput;
import app.rental.projet_3.dto.AuthDto.TokenOutput;
import app.rental.projet_3.dto.UserDto.UserOutput;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.exceptions.InvalidCredentialsException;
import app.rental.projet_3.exceptions.InvalidPasswordException;
import app.rental.projet_3.exceptions.ResourceAlreadyExistsException;
import app.rental.projet_3.exceptions.ResourceNotFoundException;
import app.rental.projet_3.repositories.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	/**
	 * Crée un utilisateur avec un mot de passe haché en BCrypt.
	 *
	 * @param input email, nom et mot de passe du nouvel utilisateur
	 * @return un token JWT pour le nouvel utilisateur
	 * @throws InvalidPasswordException si le mot de passe dépasse 72 octets (400)
	 * @throws ResourceAlreadyExistsException si l'email est déjà utilisé (409)
	 */
	public TokenOutput register(RegisterInput input) {
		// BCrypt refuse plus de 72 octets : un caractere accentue compte pour 2 octets, un emoji pour 4
		if (input.password().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new InvalidPasswordException("password : must not exceed 72 bytes");
		}

		if (userRepository.existsByEmail(input.email())) {
			throw new ResourceAlreadyExistsException("Email already used");
		}

		UserEntity user = new UserEntity();
		user.setEmail(input.email());
		user.setName(input.name());
		user.setPassword(passwordEncoder.encode(input.password()));
		userRepository.save(user);

		return new TokenOutput(jwtService.generateToken(user.getEmail()));
	}

	/**
	 * Vérifie l'email et le mot de passe avec Spring Security, puis génère un token.
	 *
	 * @param input email et mot de passe
	 * @return un token JWT pour l'utilisateur
	 * @throws InvalidCredentialsException si l'email ou le mot de passe est incorrect (401)
	 */
	public TokenOutput login(LoginInput input) {
		try {
			authenticationManager.authenticate(
					UsernamePasswordAuthenticationToken.unauthenticated(input.email(), input.password()));
		} catch (AuthenticationException e) {
			// Meme erreur pour un email inconnu ou un mauvais mot de passe :
			// on ne revele pas quels emails ont un compte
			throw new InvalidCredentialsException("Invalid email or password");
		}

		return new TokenOutput(jwtService.generateToken(input.email()));
	}

	/**
	 * Récupère l'utilisateur connecté à partir de l'email contenu dans son token.
	 *
	 * @param email l'email de l'utilisateur connecté
	 * @return l'utilisateur, sans son mot de passe
	 * @throws ResourceNotFoundException si l'utilisateur n'existe plus (404)
	 */
	public UserOutput me(String email) {
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return UserOutput.fromEntity(user);
	}
}
