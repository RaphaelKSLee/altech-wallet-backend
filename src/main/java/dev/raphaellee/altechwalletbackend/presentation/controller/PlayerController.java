package dev.raphaellee.altechwalletbackend.presentation.controller;

import dev.raphaellee.altechwalletbackend.application.dto.PlayerDto.*;
import dev.raphaellee.altechwalletbackend.application.dto.TransactionDto;
import dev.raphaellee.altechwalletbackend.application.service.PlayerServiceFacade;
import dev.raphaellee.altechwalletbackend.domain.entity.Player;
import dev.raphaellee.altechwalletbackend.domain.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerServiceFacade playerServiceFacade;

    @PostMapping
    public ResponseEntity<CreatePlayerResponse> createPlayer(
            @Valid @RequestBody CreatePlayerRequest request
    ) {
        Player player = playerService.createPlayer(request.username());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreatePlayerResponse(player.getUsername()));
    }

    @PostMapping("/{username}/wallets")
    public ResponseEntity<WalletResponse> createWallet(
            @Valid CreateWalletRequest request
    ) {
        WalletResponse response = playerServiceFacade
                .createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{username}/wallets/balance")
    public ResponseEntity<BalanceResponse> getWalletBalance(@PathVariable String username) {
        BalanceResponse response = playerServiceFacade
                .getBalance(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/transactions")
    public Page<TransactionDto.TransactionRequest> getPaginatedTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable String username) {

        return playerServiceFacade.getTransactionHistory(
                username, page, size);
    }
}