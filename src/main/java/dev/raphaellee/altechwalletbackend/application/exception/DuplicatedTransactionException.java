package dev.raphaellee.altechwalletbackend.application.exception;

import lombok.Getter;

import java.util.UUID;

public class DuplicatedTransactionException extends RuntimeException {
    @Getter
    UUID transactionId;
    public DuplicatedTransactionException(UUID uuid) {
        this.transactionId=uuid;
        super("Transaction: " + uuid + "is already processed prior.");
    }
}
