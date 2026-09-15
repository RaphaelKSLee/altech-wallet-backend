package dev.raphaellee.altechwalletbackend.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, String> {
}
