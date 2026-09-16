package dev.raphaellee.altechwalletbackend.presentation;

import java.math.BigDecimal;
import java.util.UUID;

public class Dtos {
    public record CreatePlayerRequest(String username) {}

    public record PlayerResponse(String username) {}

    public record WalletResponse(UUID walletId, String ownerUsername, BigDecimal balance, String currencyCode) {}

    public record TransactionRequest(
            UUID transactionId,
            UUID debitWalletId,
            UUID creditWalletId,
            BigDecimal amount,
            String currencyCode
    ) {}

    public record TransactionResponse(UUID transactionId) {}
}