package app.rental.projet_3.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.RentalRequestDto;
import app.rental.projet_3.entities.RentalEntity;
import app.rental.projet_3.services.RentalService;

@RestController
@RequestMapping("/rentals")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@GetMapping
	public List<RentalEntity> getAllRentals() {
		return rentalService.getAllRentals();
	}

	@GetMapping("/{id}")
	public RentalEntity getRentalById(@PathVariable Integer id) {
		return rentalService.getRentalById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RentalEntity createRental(@RequestBody RentalRequestDto rentalRequest) {
		return rentalService.createRental(rentalRequest);
	}
}
