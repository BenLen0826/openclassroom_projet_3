package app.rental.projet_3.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import app.rental.projet_3.dto.RentalDto.RentalInput;
import app.rental.projet_3.dto.RentalDto.RentalOutput;
import app.rental.projet_3.entities.RentalEntity;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.exceptions.ResourceNotFoundException;
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

	public List<RentalOutput> getAllRentals() {
		List<RentalOutput> rentals = new ArrayList<>();
		for (RentalEntity rental : rentalRepository.findAll()) {
			rentals.add(RentalOutput.fromEntity(rental));
		}
		return rentals;
	}

	public RentalOutput getRentalById(Integer id) {
		RentalEntity rental = rentalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

		return RentalOutput.fromEntity(rental);
	}

	public RentalOutput createRental(RentalInput input, String ownerEmail) {
		UserEntity owner = userRepository.findByEmail(ownerEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		RentalEntity rental = new RentalEntity();
		rental.setName(input.name());
		rental.setSurface(input.surface());
		rental.setPrice(input.price());
		// En attente du stockage de l'image : picture est un fichier, l'entite attend son URL
		// rental.setPicture(input.picture());
		rental.setDescription(input.description());
		// Le proprietaire vient du token, jamais du JSON : sinon on pourrait creer une location au nom d'un autre
		rental.setOwnerId(owner.getId());

		return RentalOutput.fromEntity(rentalRepository.save(rental));
	}
}
