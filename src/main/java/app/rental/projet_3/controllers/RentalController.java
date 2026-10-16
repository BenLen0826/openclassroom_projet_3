package app.rental.projet_3.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.RentalDto.RentalInput;
import app.rental.projet_3.dto.RentalDto.RentalOutput;
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
	public List<RentalOutput> getAllRentals() {
		return rentalService.getAllRentals();
	}

	@GetMapping("/{id}")
	public RentalOutput getRentalById(@PathVariable Integer id) {
		return rentalService.getRentalById(id);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public RentalOutput createRental(@Valid @ModelAttribute RentalInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return rentalService.createRental(input, jwt.getSubject());
	}
}
