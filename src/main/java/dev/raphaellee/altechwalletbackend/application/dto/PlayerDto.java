package dev.raphaellee.altechwalletbackend.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public class PlayerDto {

    public record CreatePlayerRequest(
            @NotBlank String username
    ) {}

    public record CreatePlayerResponse(String username) {}

    public record CreateWalletRequest(
            @NotBlank String username
    ) {}

    public record WalletResponse(UUID walletId, String ownerUsername, BigDecimal balance, String currencyCode) {}

    public record BalanceResponse(
            BigDecimal amount,
            String currency
    ) {}
}