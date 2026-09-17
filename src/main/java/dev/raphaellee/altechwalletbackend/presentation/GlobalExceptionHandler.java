package dev.raphaellee.altechwalletbackend.presentation;

import dev.raphaellee.altechwalletbackend.application.exception.DuplicatedTransactionException;
import dev.raphaellee.altechwalletbackend.domain.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedTransactionException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatedTransaction(DuplicatedTransactionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Transaction already exists", "message", ex.getMessage()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientFunds(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", "Insufficient funds", "message", ex.getMessage()));
    }
}