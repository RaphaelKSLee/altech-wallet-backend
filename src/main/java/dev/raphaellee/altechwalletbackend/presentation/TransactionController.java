package dev.raphaellee.altechwalletbackend.presentation;

import dev.raphaellee.altechwalletbackend.presentation.Dtos.*;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWallet;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWalletRepository;
import dev.raphaellee.altechwalletbackend.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final PlayerWalletRepository walletRepository;

    @PostMapping
    public ResponseEntity<TransactionResponse> performTransaction(@RequestBody TransactionRequest request) {
        // Look up both wallets. Handle missing entities gracefully in production.
        PlayerWallet debitWallet = walletRepository.findById(request.debitWalletId())
                .orElseThrow(() -> new RuntimeException("Debit wallet not found"));

        PlayerWallet creditWallet = walletRepository.findById(request.creditWalletId())
                .orElseThrow(() -> new RuntimeException("Credit wallet not found"));

        Money transactionAmount = Money.of(
                CurrencyUnit.of(request.currencyCode()),
                request.amount()
        );

        UUID transactionId = transactionService.performTransaction(
                request.transactionId(),
                debitWallet,
                creditWallet,
                transactionAmount
        );

        return ResponseEntity.ok(new TransactionResponse(transactionId));
    }
}