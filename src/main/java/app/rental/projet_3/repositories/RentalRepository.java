package app.rental.projet_3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.rental.projet_3.entities.RentalEntity;

public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {

}
