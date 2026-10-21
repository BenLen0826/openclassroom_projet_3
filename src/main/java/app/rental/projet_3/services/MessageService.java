package app.rental.projet_3.services;

import org.springframework.stereotype.Service;

import app.rental.projet_3.dto.MessageDto.MessageInput;
import app.rental.projet_3.dto.ResponseDto.MessageResponse;
import app.rental.projet_3.entities.MessageEntity;
import app.rental.projet_3.entities.UserEntity;
import app.rental.projet_3.exceptions.ResourceNotFoundException;
import app.rental.projet_3.repositories.MessageRepository;
import app.rental.projet_3.repositories.RentalRepository;
import app.rental.projet_3.repositories.UserRepository;

@Service
public class MessageService {

	private final MessageRepository messageRepository;

	private final UserRepository userRepository;

	private final RentalRepository rentalRepository;

	public MessageService(MessageRepository messageRepository, UserRepository userRepository,
			RentalRepository rentalRepository) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
		this.rentalRepository = rentalRepository;
	}

	/**
	 * Enregistre un message envoyé par l'utilisateur connecté sur une location.
	 *
	 * @param input le message et l'id de la location
	 * @param senderEmail l'email de l'utilisateur connecté, qui devient l'expéditeur
	 * @return un message de confirmation
	 * @throws ResourceNotFoundException si l'utilisateur ou la location n'existe pas (404)
	 */
	public MessageResponse sendMessage(MessageInput input, String senderEmail) {
		UserEntity sender = userRepository.findByEmail(senderEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (!rentalRepository.existsById(input.rentalId())) {
			throw new ResourceNotFoundException("Rental not found");
		}

		MessageEntity message = new MessageEntity();
		message.setMessage(input.message());
		message.setRentalId(input.rentalId());
		// L'expediteur vient du token, jamais du JSON car il peut y avoir une usurpation d'identité
		message.setUserId(sender.getId());
		messageRepository.save(message);

		return new MessageResponse("Message send with success");
	}
}
