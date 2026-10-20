package app.rental.projet_3.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.RentalDto.RentalInput;
import app.rental.projet_3.dto.RentalDto.RentalListOutput;
import app.rental.projet_3.dto.RentalDto.RentalOutput;
import app.rental.projet_3.dto.RentalDto.RentalUpdateInput;
import app.rental.projet_3.dto.ResponseDto.MessageResponse;
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
	public RentalListOutput getAllRentals() {
		return new RentalListOutput(rentalService.getAllRentals());
	}

	@GetMapping("/{id}")
	public RentalOutput getRentalById(@PathVariable Integer id) {
		return rentalService.getRentalById(id);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public MessageResponse createRental(@Valid @ModelAttribute RentalInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return rentalService.createRental(input, jwt.getSubject());
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public MessageResponse updateRental(@PathVariable Integer id,
			@Valid @ModelAttribute RentalUpdateInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return rentalService.updateRental(id, input, jwt.getSubject());
	}
}
