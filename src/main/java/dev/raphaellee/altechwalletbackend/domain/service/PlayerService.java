package dev.raphaellee.altechwalletbackend.domain.service;

import dev.raphaellee.altechwalletbackend.domain.entity.*;
import dev.raphaellee.altechwalletbackend.domain.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PlayerWalletRepository walletRepository;

    public Player createPlayer(String username) {
        Player player = new Player(username);
        playerRepository.save(player);
        return player;
    }

    public PlayerWallet createWallet(Player owner) {
        PlayerWallet wallet = PlayerWallet.builder()
                .owner(owner)
                .balance(Money.of(
                        CurrencyUnit.USD, BigDecimal.valueOf(0)))
                .build();
        walletRepository.save(wallet);
        return wallet;
    }

    public Money getBalance(Player owner) {
        PlayerWallet wallet = walletRepository.findByOwner(owner).orElseThrow(() -> new EntityNotFoundException("Player has no wallet"));
        return wallet.getBalance();
    }
}
