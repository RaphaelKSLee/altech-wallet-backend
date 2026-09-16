package dev.raphaellee.altechwalletbackend.domain.exception;

import java.util.UUID;

public class DuplicatedTransactionException extends RuntimeException {
    public DuplicatedTransactionException(UUID uuid) {
        super("Transaction: " + uuid + "is already processed prior.");
    }
}
