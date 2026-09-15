package dev.raphaellee.altechwalletbackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.money.Money;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionHistory{

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender", nullable = false, updatable = false)
    private Player sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", nullable = false, updatable = false)
    private Player receiver;

    @Embedded
    private MoneyWrapper amount;

    @CreationTimestamp
    private Instant created;
}
