package dev.raphaellee.altechwalletbackend.domain.service;

import dev.raphaellee.altechwalletbackend.domain.entity.*;
import dev.raphaellee.altechwalletbackend.domain.exception.InsufficientFundsException;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    PlayerWalletRepository walletRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    public void creditWallet(PlayerWallet wallet, Money credit) {
        wallet.setBalance(wallet.getBalance().plus(credit));
        walletRepository.save(wallet);
    }

    public void debitWallet(PlayerWallet wallet, Money debit) {
        Money balance = wallet.getBalance();
        if (balance.isLessThan(debit)) {
            throw new InsufficientFundsException(debit, balance);
        }
        wallet.setBalance(balance.minus(debit));
        walletRepository.save(wallet);
    }

    public UUID performWalletTransaction(
            UUID transactionId,
            PlayerWallet debitWallet,
            PlayerWallet creditWallet,
            Money amount
    ) {
        debitWallet(debitWallet, amount);
        creditWallet(creditWallet, amount);
        TransactionHistory history = TransactionHistory.builder()
                .transactionId(transactionId)
                .debitWallet(debitWallet)
                .creditWallet(creditWallet)
                .amount(amount)
                .build();
        history = transactionHistoryRepository.save(history);
        return history.getTransactionId();
    }
}
