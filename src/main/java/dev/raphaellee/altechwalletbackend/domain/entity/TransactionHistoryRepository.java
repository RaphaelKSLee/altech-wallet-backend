package dev.raphaellee.altechwalletbackend.domain.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionHistoryRepository extends
        JpaRepository<TransactionHistory, UUID> {

    Page<TransactionHistory> findAllByCreditWallet_OwnerOrDebitWallet_Owner(Player receiver, Player sender, Pageable pageable);
    default Page<TransactionHistory>findAllByPlayerInvolvement(Player player, Pageable pageable) {
        return findAllByCreditWallet_OwnerOrDebitWallet_Owner(player, player, pageable);
    }
}
