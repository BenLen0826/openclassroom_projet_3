package app.rental.projet_3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.rental.projet_3.dto.MessageDto.MessageInput;
import app.rental.projet_3.dto.ResponseDto.MessageResponse;
import app.rental.projet_3.services.MessageService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Envoi de messages aux propriétaires")
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostMapping
	@Operation(summary = "Envoyer un message au propriétaire d'une location")
	public MessageResponse sendMessage(@Valid @RequestBody MessageInput input,
			@AuthenticationPrincipal Jwt jwt) {
		return messageService.sendMessage(input, jwt.getSubject());
	}
}
