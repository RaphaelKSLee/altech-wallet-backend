package dev.raphaellee.altechwalletbackend.domain.service;

import dev.raphaellee.altechwalletbackend.domain.entity.Player;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerRepository;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWallet;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWalletRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerWalletRepository walletRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void shouldCreateAndSavePlayer() {
        String username = "alice";

        Player createdPlayer = playerService.createPlayer(username);

        assertThat(createdPlayer.getUsername()).isEqualTo(username);
        verify(playerRepository).save(createdPlayer);
    }

    @Test
    void shouldCreateAndSaveWalletWithZeroUSDBalance() {
        Player owner = new Player("bob");

        PlayerWallet createdWallet = playerService.createWallet(owner);

        assertThat(createdWallet.getOwner()).isEqualTo(owner);
        assertThat(createdWallet.getBalance())
                .isEqualTo(Money.of(CurrencyUnit.USD, BigDecimal.valueOf(0)));
        verify(walletRepository).save(createdWallet);
    }
}