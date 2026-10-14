package app.rental.projet_3.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.rental.projet_3.dto.RentalRequestDto;
import app.rental.projet_3.entities.RentalEntity;
import app.rental.projet_3.repositories.RentalRepository;

@Service
public class RentalService {

	private final RentalRepository rentalRepository;

	public RentalService(RentalRepository rentalRepository) {
		this.rentalRepository = rentalRepository;
	}

	public List<RentalEntity> getAllRentals() {
		return rentalRepository.findAll();
	}

	public RentalEntity getRentalById(Integer id) {
		return rentalRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
	}

	public RentalEntity createRental(RentalRequestDto rentalRequest) {
		RentalEntity rental = new RentalEntity();
		rental.setName(rentalRequest.getName());
		rental.setSurface(rentalRequest.getSurface());
		rental.setPrice(rentalRequest.getPrice());
		rental.setPicture(rentalRequest.getPicture());
		rental.setDescription(rentalRequest.getDescription());
		rental.setOwnerId(rentalRequest.getOwnerId());
		return rentalRepository.save(rental);
	}
}
