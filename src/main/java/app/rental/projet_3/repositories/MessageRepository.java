package app.rental.projet_3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.rental.projet_3.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

}
