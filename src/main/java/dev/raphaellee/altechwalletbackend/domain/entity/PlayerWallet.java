package dev.raphaellee.altechwalletbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.joda.money.Money;

import java.util.UUID;

@Entity
@Builder @Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerWallet {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_username", nullable = false, updatable = false)
    private Player owner;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Embedded
    private MoneyWrapper balance;

    @Version
    private long version;

    // Custom Getter for the Service Layer
    public Money getBalance() {
        return this.balance.toMoney();
    }

    // Custom Setter for the Service Layer
    public void setBalance(Money money) {
        this.balance = new MoneyWrapper(money);
    }

    // Custom Builder Method
    public static class PlayerWalletBuilder {
        // Lombok will wire this up automatically
        public PlayerWalletBuilder balance(Money money) {
            this.balance = new MoneyWrapper(money);
            return this;
        }
    }
}
