package app.rental.projet_3.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.AuthDto;
import app.rental.projet_3.dto.UserDto;
import app.rental.projet_3.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public AuthDto.TokenOutput register(@RequestBody AuthDto.RegisterInput input) {
		return authService.register(input);
	}

	@PostMapping("/login")
	public AuthDto.TokenOutput login(@RequestBody AuthDto.LoginInput input) {
		return authService.login(input);
	}

	@GetMapping("/me")
	public UserDto.UserOutput me(@AuthenticationPrincipal Jwt jwt) {
		return authService.me(jwt.getSubject());
	}
}
