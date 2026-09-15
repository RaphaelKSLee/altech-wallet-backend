package dev.raphaellee.altechwalletbackend.domain.entity;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlayerWalletRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PlayerWalletRepository walletRepository;

    @Test
    void shouldFindWalletByUsername() {
        Player player = new Player("charlie");
        entityManager.persist(player);

        PlayerWallet wallet = PlayerWallet.builder()
                .owner(player)
                .balance(Money.of(
                        CurrencyUnit.USD, BigDecimal.valueOf(100.00)))
                .build();
        entityManager.persistAndFlush(wallet);
        entityManager.clear();

        Optional<PlayerWallet> foundWallet = walletRepository
                .findByOwner_Username("charlie");

        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getBalance())
                .isEqualTo(Money.of(CurrencyUnit.USD, BigDecimal.valueOf(100.00)));
    }


}