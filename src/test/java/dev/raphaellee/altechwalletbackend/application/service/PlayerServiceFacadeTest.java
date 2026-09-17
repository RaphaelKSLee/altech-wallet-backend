package dev.raphaellee.altechwalletbackend.application.service;

import dev.raphaellee.altechwalletbackend.application.dto.PlayerDto;
import dev.raphaellee.altechwalletbackend.application.dto.TransactionDto;
import dev.raphaellee.altechwalletbackend.domain.entity.*;
import dev.raphaellee.altechwalletbackend.domain.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceFacadeTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private PlayerServiceFacade playerServiceFacade;

    @Test
    void createWallet_ShouldReturnWalletResponse() {
        // Arrange
        String username = "testuser";
        UUID expectedWalletId = UUID.randomUUID();
        BigDecimal expectedAmount = BigDecimal.valueOf(100.00);
        String expectedCurrency = "USD";

        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getUsername()).thenReturn(username);

        PlayerWallet mockWallet = mock(PlayerWallet.class, Answers.RETURNS_DEEP_STUBS);
        when(mockWallet.getWalletId()).thenReturn(expectedWalletId);
        when(mockWallet.getOwner()).thenReturn(mockPlayer);
        when(mockWallet.getBalance().getAmount()).thenReturn(expectedAmount);
        when(mockWallet.getBalance().getCurrencyUnit().getCode()).thenReturn(expectedCurrency);

        when(playerRepository.findByIdOrThrow(username)).thenReturn(mockPlayer);
        when(playerService.createWallet(mockPlayer)).thenReturn(mockWallet);

        // Act
        PlayerDto.WalletResponse response = playerServiceFacade
                .createWallet(new PlayerDto.CreateWalletRequest(username));

        // Assert
        assertNotNull(response);
        assertEquals(expectedWalletId, response.walletId());
        assertEquals(username, response.ownerUsername());
        assertEquals(expectedAmount, response.balance());

        verify(playerRepository).findByIdOrThrow(username);
        verify(playerService).createWallet(mockPlayer);
    }

    @Test
    void getBalance_ShouldReturnBalanceResponse() {
        // Arrange
        String username = "testuser";
        BigDecimal expectedAmount = BigDecimal.valueOf(250.50);
        String expectedCurrency = "EUR";

        Player mockPlayer = mock(Player.class);

        PlayerWallet mockWallet = mock(PlayerWallet.class, Answers.RETURNS_DEEP_STUBS);
        when(mockWallet.getBalance().getAmount()).thenReturn(expectedAmount);
        when(mockWallet.getBalance().getCurrencyUnit().toString()).thenReturn(expectedCurrency);

        when(playerRepository.findByIdOrThrow(username)).thenReturn(mockPlayer);
        when(playerService.getWallet(mockPlayer)).thenReturn(mockWallet);

        // Act
        PlayerDto.BalanceResponse response = playerServiceFacade.getBalance(username);

        // Assert
        assertNotNull(response);
        assertEquals(expectedAmount, response.amount());

        verify(playerRepository).findByIdOrThrow(username);
        verify(playerService).getWallet(mockPlayer);
    }
}