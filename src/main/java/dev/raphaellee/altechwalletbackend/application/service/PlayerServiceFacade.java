package dev.raphaellee.altechwalletbackend.application.service;

import dev.raphaellee.altechwalletbackend.application.dto.PlayerDto;
import dev.raphaellee.altechwalletbackend.application.dto.TransactionDto;
import dev.raphaellee.altechwalletbackend.domain.entity.*;
import dev.raphaellee.altechwalletbackend.domain.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PlayerServiceFacade {

    @Autowired
    PlayerService playerService;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;

    public PlayerDto.CreatePlayerResponse createPlayer(
            PlayerDto.CreatePlayerRequest request
    ) {
        Player player = playerService.createPlayer(request.username());
        return new PlayerDto.CreatePlayerResponse(player.getUsername());
    }

    public PlayerDto.WalletResponse createWallet(PlayerDto.CreateWalletRequest request) {
        Player owner = playerRepository.findByIdOrThrow(request.username());
        PlayerWallet wallet = playerService.createWallet(owner);
        return new PlayerDto.WalletResponse(
                wallet.getWalletId(),
                wallet.getOwner().getUsername(),
                wallet.getBalance().getAmount(),
                wallet.getBalance().getCurrencyUnit().getCode()
        );
    }
    public PlayerDto.BalanceResponse getBalance(String username) {
        Player owner = playerRepository.findByIdOrThrow(username);
        PlayerWallet wallet = playerService.getWallet(owner);
        return new PlayerDto.BalanceResponse(
                wallet.getBalance().getAmount(),
                wallet.getBalance().getCurrencyUnit().toString()
        );
    }
    public Page<TransactionDto.TransactionRequest> getTransactionHistory(
            String username,
            int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Player player = playerService.getPlayer(username);
        return transactionHistoryRepository
                .findAllByPlayerInvolvement(player, pageable)
                .map(TransactionDto.TransactionRequest::fromEntity);
    }
}
