package dev.raphaellee.altechwalletbackend.domain.entity;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, String> {
    default Player findByIdOrThrow(String username) {
        return findById(username).orElseThrow(EntityNotFoundException::new);
    }
}
