package app.rental.projet_3.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.RentalDto;
import app.rental.projet_3.services.RentalService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@GetMapping
	public List<RentalDto.RentalOutput> getAllRentals() {
		return rentalService.getAllRentals();
	}

	@GetMapping("/{id}")
	public RentalDto.RentalOutput getRentalById(@PathVariable Integer id) {
		return rentalService.getRentalById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RentalDto.RentalOutput createRental(@Valid @RequestBody RentalDto.RentalInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return rentalService.createRental(input, jwt.getSubject());
	}
}
