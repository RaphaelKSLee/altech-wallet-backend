package dev.raphaellee.altechwalletbackend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.money.Money;

import java.time.Instant;
import java.util.UUID;

@Entity
@Builder @Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionHistory{

    @Id
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_wallet", nullable = false, updatable = false)
    private PlayerWallet debitWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_wallet", nullable = false, updatable = false)
    private PlayerWallet creditWallet;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Embedded
    private MoneyWrapper amount;

    @CreationTimestamp
    private Instant created;

    // Custom Getter
    public Money getAmount() {
        return this.amount.toMoney();
    }

    // Custom Setter
    public void setAmount(Money money) {
        this.amount = new MoneyWrapper(money);
    }

    // Custom Builder Method
    public static class TransactionHistoryBuilder {
        // Lombok will wire this up automatically
        public TransactionHistoryBuilder amount(Money money) {
            this.amount = new MoneyWrapper(money);
            return this;
        }
    }
}
