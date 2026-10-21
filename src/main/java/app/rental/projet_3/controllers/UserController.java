package app.rental.projet_3.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.UserDto.UserOutput;
import app.rental.projet_3.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Users", description = "Consultation des utilisateurs")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Récupère un utilisateur à partir de son id.
	 *
	 * @param id l'id de l'utilisateur
	 * @return l'utilisateur, sans son mot de passe (erreur 404 s'il n'existe pas)
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Récupérer un utilisateur par son ID")
	public UserOutput getUserById(@PathVariable Integer id) {
		return userService.getUserById(id);
	}
}
