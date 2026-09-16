package dev.raphaellee.altechwalletbackend.domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EntityIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldPersistPlayer() {
        Player player = new Player("player");
        entityManager.persist(player);

        entityManager.flush();
        entityManager.clear();

        Player savedPlayer = entityManager
                .find(Player.class, player.getUsername());

        log.info("shouldPersistPlayer - " +
                "player: {}", player);


        assertThat(savedPlayer).isNotNull();
        assertThat(savedPlayer.getUsername()).isEqualTo("player");
    }

    @Test
    void shouldPersistAndRetrieveMoneyCorrectly() {

        Player sender = new Player("alice");
        entityManager.persist(sender);
        PlayerWallet debitWallet = PlayerWallet.builder()
                .owner(sender)
                .balance(Money.of(
                        CurrencyUnit.USD, BigDecimal.valueOf(100.55)))
                .build();
        entityManager.persist(debitWallet);

        Player receiver = new Player("bob");
        entityManager.persist(receiver);
        PlayerWallet creditWallet = PlayerWallet.builder()
                .owner(receiver)
                .balance(Money.of(
                        CurrencyUnit.USD, BigDecimal.valueOf(0)))
                .build();
        entityManager.persist(creditWallet);

        TransactionHistory history = TransactionHistory.builder()
                .transactionId(UUID.randomUUID())
                .debitWallet(debitWallet).creditWallet(creditWallet)
                .amount(Money.of(
                        CurrencyUnit.USD, BigDecimal.valueOf(25.00)))
                .created(Instant.now())
                .build();

        entityManager.persist(history);

        entityManager.flush();
        entityManager.clear();

        PlayerWallet savedWallet = entityManager
                .find(PlayerWallet.class, debitWallet.getWalletId());
        assertThat(savedWallet).isNotNull();
        assertThat(savedWallet.getOwner().getUsername())
                .isEqualTo("alice");

        Money savedMoney = savedWallet.getBalance();
        assertThat(savedMoney.getCurrencyUnit()).isEqualTo(CurrencyUnit.USD);
        assertThat(savedMoney.getAmount())
                .isEqualByComparingTo(BigDecimal.valueOf(100.55));

        TransactionHistory savedHistory = entityManager
                .find(TransactionHistory.class, history.getTransactionId());
        assertThat(savedHistory).isNotNull();
        assertThat(savedHistory.getAmount())
                .isEqualTo(Money.of(CurrencyUnit.USD, BigDecimal.valueOf(25.00)));
    }

}