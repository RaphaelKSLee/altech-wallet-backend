package dev.raphaellee.altechwalletbackend.domain.service;

import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWallet;
import dev.raphaellee.altechwalletbackend.domain.entity.PlayerWalletRepository;
import dev.raphaellee.altechwalletbackend.domain.entity.TransactionHistory;
import dev.raphaellee.altechwalletbackend.domain.entity.TransactionHistoryRepository;
import dev.raphaellee.altechwalletbackend.application.exception.DuplicatedTransactionException;
import dev.raphaellee.altechwalletbackend.domain.exception.InsufficientFundsException;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private PlayerWalletRepository walletRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private PlayerWallet debitWallet;
    private PlayerWallet creditWallet;
    private UUID transactionId;
    private Money transferAmount;

    @BeforeEach
    void setUp() {
        debitWallet = PlayerWallet.builder()
                .balance(Money.of(CurrencyUnit.USD, 100.00))
                .build();

        creditWallet = PlayerWallet.builder()
                .balance(Money.of(CurrencyUnit.USD, 50.00))
                .build();

        transactionId = UUID.randomUUID();
        transferAmount = Money.of(CurrencyUnit.USD, 25.00);
    }

    @Test
    void creditWallet_ShouldIncreaseBalanceAndSave() {
        transactionService.creditWallet(creditWallet, transferAmount);

        assertEquals(Money.of(CurrencyUnit.USD, 75.00), creditWallet.getBalance());
        verify(walletRepository, times(1)).save(creditWallet);
    }

    @Test
    void debitWallet_ShouldDecreaseBalanceAndSave_WhenFundsSufficient() {
        transactionService.debitWallet(debitWallet, transferAmount);

        assertEquals(Money.of(CurrencyUnit.USD, 75.00), debitWallet.getBalance());
        verify(walletRepository, times(1)).save(debitWallet);
    }

    @Test
    void debitWallet_ShouldThrowException_WhenFundsInsufficient() {
        Money excessiveAmount = Money.of(CurrencyUnit.USD, 150.00);

        assertThrows(InsufficientFundsException.class, () ->
                transactionService.debitWallet(debitWallet, excessiveAmount)
        );
        verify(walletRepository, never()).save(any());
    }

    @Test
    void performWalletTransaction_ShouldTransferFundsAndSaveHistory_WhenValid() {
        // Arrange
        when(transactionHistoryRepository.save(any(TransactionHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UUID resultId = transactionService.performWalletTransaction(transactionId, debitWallet, creditWallet, transferAmount);

        // Assert
        assertEquals(transactionId, resultId);
        assertEquals(Money.of(CurrencyUnit.USD, 75.00), debitWallet.getBalance());
        assertEquals(Money.of(CurrencyUnit.USD, 75.00), creditWallet.getBalance());

        verify(walletRepository, times(1)).save(debitWallet);
        verify(walletRepository, times(1)).save(creditWallet);

        ArgumentCaptor<TransactionHistory> historyCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
        verify(transactionHistoryRepository).save(historyCaptor.capture());

        TransactionHistory savedHistory = historyCaptor.getValue();
        assertEquals(transactionId, savedHistory.getTransactionId());
        assertEquals(transferAmount, savedHistory.getAmount());
    }
}