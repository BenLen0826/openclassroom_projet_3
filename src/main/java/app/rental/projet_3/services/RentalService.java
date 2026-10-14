package app.rental.projet_3.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import app.rental.projet_3.dto.RentalRequestDto;
import app.rental.projet_3.entities.RentalEntity;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.repositories.RentalRepository;
import app.rental.projet_3.repositories.UserRepository;

@Service
public class RentalService {

	private final RentalRepository rentalRepository;

	private final UserRepository userRepository;

	public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
		this.rentalRepository = rentalRepository;
		this.userRepository = userRepository;
	}

	public List<RentalEntity> getAllRentals() {
		return rentalRepository.findAll();
	}

	public RentalEntity getRentalById(Integer id) {
		return rentalRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));
	}

	public RentalEntity createRental(RentalRequestDto rentalRequest, String ownerEmail) {
		UserEntity owner = userRepository.findByEmail(ownerEmail)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		RentalEntity rental = new RentalEntity();
		rental.setName(rentalRequest.getName());
		rental.setSurface(rentalRequest.getSurface());
		rental.setPrice(rentalRequest.getPrice());
		rental.setPicture(rentalRequest.getPicture());
		rental.setDescription(rentalRequest.getDescription());
		// Le proprietaire vient du token, jamais du JSON : sinon on pourrait creer une location au nom d'un autre
		rental.setOwnerId(owner.getId());
		return rentalRepository.save(rental);
	}
}
