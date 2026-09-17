package dev.raphaellee.altechwalletbackend.application.dto;

import dev.raphaellee.altechwalletbackend.domain.entity.TransactionHistory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionDto {

    public record TransactionRequest(
            @NotBlank UUID transactionId,
            @NotBlank String senderUsername,
            @NotBlank String receiverUsername,
            @PositiveOrZero @Digits(integer = 15, fraction = 2)
            @NotNull BigDecimal amount
    ) {
        public static TransactionRequest fromEntity(
                TransactionHistory history
        ) {
            return new TransactionRequest(
                    history.getTransactionId(),
                    history.getDebitWallet().getWalletId().toString(),
                    history.getCreditWallet().getWalletId().toString(),
                    history.getAmount().getAmount()
            );
        }
    }

    public record TransactionResponse(UUID transactionId) {}
}
