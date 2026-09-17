package dev.raphaellee.altechwalletbackend.presentation.controller;

import dev.raphaellee.altechwalletbackend.application.service.TransactionServiceFacade;
import dev.raphaellee.altechwalletbackend.application.dto.TransactionDto.*;
import dev.raphaellee.altechwalletbackend.domain.entity.TransactionHistory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    @Autowired
    private TransactionServiceFacade transactionServiceFacade;

    @PostMapping
    public ResponseEntity<TransactionResponse> performTransaction(
            @Valid @RequestBody TransactionRequest request
    ) {

        UUID transactionId = transactionServiceFacade
                .performPlayerIdempotentTransaction(request);

        return ResponseEntity.ok(new TransactionResponse(transactionId));
    }
}