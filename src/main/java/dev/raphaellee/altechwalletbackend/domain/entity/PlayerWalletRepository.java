package dev.raphaellee.altechwalletbackend.domain.entity;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayerWalletRepository extends
        JpaRepository<PlayerWallet, UUID> {
    Optional<PlayerWallet> findByOwner_Username(String username);
    default PlayerWallet findByOwner_UsernameOrThrow(String username) {
        return findByOwner_Username(username).orElseThrow(EntityNotFoundException::new);
    }
    Optional<PlayerWallet> findByOwner(Player owner);
    default PlayerWallet findByOwnerOrThrow(Player owner) {
        return findByOwner(owner).orElseThrow(EntityNotFoundException::new);
    }
}
