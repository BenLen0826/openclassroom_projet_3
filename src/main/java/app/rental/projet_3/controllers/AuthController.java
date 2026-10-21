package app.rental.projet_3.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.AuthDto.LoginInput;
import app.rental.projet_3.dto.AuthDto.RegisterInput;
import app.rental.projet_3.dto.AuthDto.TokenOutput;
import app.rental.projet_3.dto.UserDto.UserOutput;
import app.rental.projet_3.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Inscription, connexion et utilisateur connecté")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * Crée un compte utilisateur, avec un mot de passe haché en base.
	 *
	 * @param input email, nom et mot de passe du nouvel utilisateur
	 * @return un token JWT pour être connecté directement
	 */
	@PostMapping("/register")
	@Operation(summary = "Créer un compte et récupérer un token JWT")
	public TokenOutput register(@Valid @RequestBody RegisterInput input) {
		return authService.register(input);
	}

	/**
	 * Connecte un utilisateur à partir de son email et de son mot de passe.
	 *
	 * @param input email et mot de passe
	 * @return un token JWT à envoyer ensuite dans l'en-tête Authorization
	 */
	@PostMapping("/login")
	@Operation(summary = "Se connecter et récupérer un token JWT")
	public TokenOutput login(@Valid @RequestBody LoginInput input) {
		return authService.login(input);
	}

	/**
	 * Récupère les informations de l'utilisateur connecté.
	 *
	 * @param jwt le token de l'utilisateur, qui contient son email
	 * @return l'utilisateur connecté, sans son mot de passe
	 */
	@GetMapping("/me")
	@Operation(summary = "Récupérer l'utilisateur connecté")
	public UserOutput me(@AuthenticationPrincipal Jwt jwt) {
		return authService.me(jwt.getSubject());
	}
}
