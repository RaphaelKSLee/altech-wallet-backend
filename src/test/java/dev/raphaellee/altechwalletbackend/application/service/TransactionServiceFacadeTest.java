package dev.raphaellee.altechwalletbackend.application.service;

import dev.raphaellee.altechwalletbackend.application.dto.TransactionDto;
import dev.raphaellee.altechwalletbackend.application.exception.DuplicatedTransactionException;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWallet;
import dev.raphaellee.altechwalletbackend.domain.entity.TransactionHistoryRepository;
import dev.raphaellee.altechwalletbackend.domain.service.PlayerService;
import dev.raphaellee.altechwalletbackend.domain.service.TransactionService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceFacadeTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private TransactionServiceFacade transactionServiceFacade;

    @Test
    void performPlayerIdempotentTransaction_SuccessfulTransaction() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        String sender = "player1";
        String receiver = "player2";
        BigDecimal amount = new BigDecimal("50.00");

        // Assuming TransactionRequest is a record or has an all-args constructor
        TransactionDto.TransactionRequest request = new TransactionDto.TransactionRequest(
                transactionId, sender, receiver, amount
        );

        PlayerWallet senderWallet = mock(PlayerWallet.class);
        PlayerWallet receiverWallet = mock(PlayerWallet.class);
        UUID expectedResultId = UUID.randomUUID();

        // Mock repository to simulate a new, non-duplicated transaction
        when(transactionHistoryRepository.existsById(transactionId)).thenReturn(false);

        // Mock player service wallet retrieval
        when(playerService.getWallet(sender)).thenReturn(senderWallet);
        when(playerService.getWallet(receiver)).thenReturn(receiverWallet);

        // Mock the actual transaction processing
        when(transactionService.performWalletTransaction(
                eq(transactionId),
                eq(senderWallet),
                eq(receiverWallet),
                eq(Money.of(CurrencyUnit.USD, amount))
        )).thenReturn(expectedResultId);

        // Act
        UUID result = transactionServiceFacade.performPlayerIdempotentTransaction(request);

        // Assert
        assertEquals(expectedResultId, result);

        // Verify interactions
        verify(transactionHistoryRepository).existsById(transactionId);
        verify(playerService).getWallet(sender);
        verify(playerService).getWallet(receiver);
        verify(transactionService).performWalletTransaction(
                transactionId, senderWallet, receiverWallet, Money.of(CurrencyUnit.USD, amount)
        );
    }

    @Test
    void performPlayerIdempotentTransaction_ThrowsDuplicatedTransactionException() {
        // Arrange
        UUID transactionId = UUID.randomUUID();
        TransactionDto.TransactionRequest request = new TransactionDto.TransactionRequest(
                transactionId, "sender", "receiver", new BigDecimal("50.00")
        );

        // Mock repository to simulate that the transaction already exists
        when(transactionHistoryRepository.existsById(transactionId)).thenReturn(true);

        // Act & Assert
        DuplicatedTransactionException exception = assertThrows(
                DuplicatedTransactionException.class,
                () -> transactionServiceFacade.performPlayerIdempotentTransaction(request)
        );

        assertEquals(transactionId, exception.getTransactionId()); // Assuming your exception exposes the ID

        // Verify that execution stops before fetching wallets or performing the transaction
        verify(transactionHistoryRepository).existsById(transactionId);
        verifyNoInteractions(playerService);
        verifyNoInteractions(transactionService);
    }
}