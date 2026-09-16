package dev.raphaellee.altechwalletbackend.presentation;

import dev.raphaellee.altechwalletbackend.presentation.Dtos.*;
import dev.raphaellee.altechwalletbackend.domain.entity.Player;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerRepository;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWallet;
import dev.raphaellee.altechwalletbackend.domain.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@RequestBody CreatePlayerRequest request) {
        Player player = playerService.createPlayer(request.username());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PlayerResponse(player.getUsername()));
    }

    @PostMapping("/{username}/wallets")
    public ResponseEntity<WalletResponse> createWallet(@PathVariable String username) {
        // Assuming PlayerRepository has findById. In a real app, handle the Optional empty case properly.
        Player owner = playerRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        PlayerWallet wallet = playerService.createWallet(owner);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new WalletResponse(
                        wallet.getWalletId(),
                        wallet.getOwner().getUsername(),
                        wallet.getBalance().getAmount(),
                        wallet.getBalance().getCurrencyUnit().getCode()
                ));
    }

    @GetMapping("/{username}/wallets/balance")
    public ResponseEntity<BalanceResponse> getWalletBalance(@PathVariable String username) {
        // Find the player or throw an exception if they don't exist
        Player owner = playerRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Retrieve the balance using the PlayerService
        org.joda.money.Money balance = playerService.getBalance(owner);

        // Return the response
        return ResponseEntity.ok(new BalanceResponse(
                balance.getAmount(),
                balance.getCurrencyUnit().getCode()
        ));
    }
}