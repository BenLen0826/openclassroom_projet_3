package app.rental.projet_3.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.UserDto.UserOutput;
import app.rental.projet_3.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/{id}")
	public UserOutput getUserById(@PathVariable Integer id) {
		return userService.getUserById(id);
	}
}
