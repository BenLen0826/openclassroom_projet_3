package app.rental.projet_3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.UserDto.UserOutput;
import app.rental.projet_3.services.UserService;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Users", description = "Consultation des utilisateurs")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Récupérer un utilisateur par son ID")
	public UserOutput getUserById(@PathVariable Integer id) {
		return userService.getUserById(id);
	}
}
