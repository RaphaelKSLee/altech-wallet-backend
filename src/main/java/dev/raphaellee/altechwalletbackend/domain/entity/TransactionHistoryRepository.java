package dev.raphaellee.altechwalletbackend.domain.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionHistoryRepository extends
        JpaRepository<TransactionHistory, UUID> {
}
