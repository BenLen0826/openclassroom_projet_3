package app.rental.projet_3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Rentals", description = "Gestion des locations")
public class RentalController {

	private final RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	/**
	 * Récupère toutes les locations.
	 *
	 * @return la liste des locations, dans un objet { rentals: [...] }
	 */
	@GetMapping
	@Operation(summary = "Récupérer toutes les locations")
	public RentalListOutput getAllRentals() {
		return new RentalListOutput(rentalService.getAllRentals());
	}

	/**
	 * Récupère une location à partir de son id.
	 *
	 * @param id l'id de la location
	 * @return la location (erreur 404 si elle n'existe pas)
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Récupérer une location par son ID")
	public RentalOutput getRentalById(@PathVariable Integer id) {
		return rentalService.getRentalById(id);
	}

	/**
	 * Crée une location, dont le propriétaire est l'utilisateur connecté.
	 *
	 * @param input les champs du formulaire, dont l'image
	 * @param jwt le token de l'utilisateur connecté
	 * @return un message de confirmation
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Créer une nouvelle location")
	public MessageResponse createRental(@Valid @ModelAttribute RentalInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return rentalService.createRental(input, jwt.getSubject());
	}

	/**
	 * Modifie une location. Seul son propriétaire peut le faire (sinon erreur 403).
	 *
	 * @param id l'id de la location
	 * @param input les nouveaux champs du formulaire (l'image ne peut pas être modifiée)
	 * @param jwt le token de l'utilisateur connecté
	 * @return un message de confirmation
	 */
	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Mettre à jour une location existante")
	public MessageResponse updateRental(@PathVariable Integer id,
			@Valid @ModelAttribute RentalUpdateInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return rentalService.updateRental(id, input, jwt.getSubject());
	}
}
