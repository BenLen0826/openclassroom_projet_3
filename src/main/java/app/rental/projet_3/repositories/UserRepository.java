package app.rental.projet_3.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.rental.projet_3.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> findByEmail(String email);

	boolean existsByEmail(String email);
}
