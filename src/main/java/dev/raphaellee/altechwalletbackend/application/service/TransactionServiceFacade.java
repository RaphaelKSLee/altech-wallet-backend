package dev.raphaellee.altechwalletbackend.application.service;

import dev.raphaellee.altechwalletbackend.application.dto.TransactionDto;
import dev.raphaellee.altechwalletbackend.application.exception.DuplicatedTransactionException;
import dev.raphaellee.altechwalletbackend.domain.entity.TransactionHistoryRepository;
import dev.raphaellee.altechwalletbackend.domain.service.PlayerService;
import dev.raphaellee.altechwalletbackend.domain.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class TransactionServiceFacade {
    @Autowired
    PlayerService playerService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;


    @Transactional
    public UUID performPlayerIdempotentTransaction(
            TransactionDto.TransactionRequest request
    ) {
        log.trace("performPlayerIdempotentTransaction - " +
                        "request: {}", request);
        if (transactionHistoryRepository.existsById(
                request.transactionId())) {
            throw new DuplicatedTransactionException(
                    request.transactionId());
        }
        return transactionService.performWalletTransaction(
                request.transactionId(),
                playerService.getWallet(request.senderUsername()),
                playerService.getWallet(request.receiverUsername()),
                Money.of(CurrencyUnit.USD, request.amount())
        );
    }

}
