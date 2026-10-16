package app.rental.projet_3.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.rental.projet_3.dto.AuthDto;
import app.rental.projet_3.dto.UserDto;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.exceptions.InvalidCredentialsException;
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

	public AuthDto.TokenOutput register(AuthDto.RegisterInput input) {
		if (userRepository.existsByEmail(input.email())) {
			throw new ResourceAlreadyExistsException("Email already used");
		}

		UserEntity user = new UserEntity();
		user.setEmail(input.email());
		user.setName(input.name());
		user.setPassword(passwordEncoder.encode(input.password()));
		userRepository.save(user);

		return new AuthDto.TokenOutput(jwtService.generateToken(user.getEmail()));
	}

	public AuthDto.TokenOutput login(AuthDto.LoginInput input) {
		try {
			authenticationManager.authenticate(
					UsernamePasswordAuthenticationToken.unauthenticated(input.email(), input.password()));
		} catch (AuthenticationException e) {
			// Meme erreur pour un email inconnu ou un mauvais mot de passe :
			// on ne revele pas quels emails ont un compte
			throw new InvalidCredentialsException("Invalid email or password");
		}

		return new AuthDto.TokenOutput(jwtService.generateToken(input.email()));
	}

	public UserDto.UserOutput me(String email) {
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return UserDto.UserOutput.fromEntity(user);
	}
}
