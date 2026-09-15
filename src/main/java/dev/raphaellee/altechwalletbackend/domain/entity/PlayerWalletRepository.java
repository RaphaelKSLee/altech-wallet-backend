package dev.raphaellee.altechwalletbackend.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayerWalletRepository extends
        JpaRepository<PlayerWallet, UUID> {
    Optional<PlayerWallet> findByOwner_Username(String username);
}
