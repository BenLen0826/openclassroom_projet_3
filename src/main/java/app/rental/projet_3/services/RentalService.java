package app.rental.projet_3.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import app.rental.projet_3.dto.RentalDto.RentalInput;
import app.rental.projet_3.dto.RentalDto.RentalOutput;
import app.rental.projet_3.dto.RentalDto.RentalUpdateInput;
import app.rental.projet_3.dto.ResponseDto.MessageResponse;
import app.rental.projet_3.entities.RentalEntity;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.exceptions.NotResourceOwnerException;
import app.rental.projet_3.exceptions.ResourceNotFoundException;
import app.rental.projet_3.repositories.RentalRepository;
import app.rental.projet_3.repositories.UserRepository;

@Service
public class RentalService {

	private final RentalRepository rentalRepository;

	private final UserRepository userRepository;

	private final ImageStorageService imageStorageService;

	public RentalService(RentalRepository rentalRepository, UserRepository userRepository,
			ImageStorageService imageStorageService) {
		this.rentalRepository = rentalRepository;
		this.userRepository = userRepository;
		this.imageStorageService = imageStorageService;
	}

	/**
	 * Récupère toutes les locations et les convertit en DTO.
	 *
	 * @return la liste des locations
	 */
	public List<RentalOutput> getAllRentals() {
		List<RentalOutput> rentals = new ArrayList<>();
		for (RentalEntity rental : rentalRepository.findAll()) {
			rentals.add(RentalOutput.fromEntity(rental));
		}
		return rentals;
	}

	/**
	 * Récupère une location à partir de son id.
	 *
	 * @param id l'id de la location
	 * @return la location
	 * @throws ResourceNotFoundException si la location n'existe pas (404)
	 */
	public RentalOutput getRentalById(Integer id) {
		RentalEntity rental = rentalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

		return RentalOutput.fromEntity(rental);
	}

	/**
	 * Crée une location : enregistre l'image sur le disque, puis la location en base
	 * avec l'URL complète de l'image.
	 *
	 * @param input les champs du formulaire, dont l'image
	 * @param ownerEmail l'email de l'utilisateur connecté, qui devient le propriétaire
	 * @return un message de confirmation
	 * @throws ResourceNotFoundException si l'utilisateur n'existe plus (404)
	 */
	public MessageResponse createRental(RentalInput input, String ownerEmail) {
		UserEntity owner = userRepository.findByEmail(ownerEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		RentalEntity rental = new RentalEntity();
		rental.setName(input.name());
		rental.setSurface(input.surface());
		rental.setPrice(input.price());
		String filename = imageStorageService.save(input.picture());

		String pictureUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(ImageStorageService.URL_PATH)
				.path(filename)
				.toUriString();

		rental.setPicture(pictureUrl);
		rental.setDescription(input.description());
		// Le proprietaire vient du token, jamais du JSON : sinon on pourrait creer une location au nom d'un autre
		rental.setOwnerId(owner.getId());

		rentalRepository.save(rental);

		return new MessageResponse("Rental created !");
	}

	/**
	 * Modifie une location après avoir vérifié que l'utilisateur en est le propriétaire.
	 * L'image n'est pas modifiable.
	 *
	 * @param id l'id de la location
	 * @param input les nouveaux champs du formulaire
	 * @param userEmail l'email de l'utilisateur connecté
	 * @return un message de confirmation
	 * @throws ResourceNotFoundException si la location ou l'utilisateur n'existe pas (404)
	 * @throws NotResourceOwnerException si l'utilisateur n'est pas le propriétaire (403)
	 */
	public MessageResponse updateRental(Integer id, RentalUpdateInput input, String userEmail) {
		RentalEntity rental = rentalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

		UserEntity user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (!rental.getOwnerId().equals(user.getId())) {
			throw new NotResourceOwnerException("You are not the owner of this rental");
		}

		rental.setName(input.name());
		rental.setSurface(input.surface());
		rental.setPrice(input.price());
		rental.setDescription(input.description());
		rentalRepository.save(rental);

		return new MessageResponse("Rental updated !");
	}
}
