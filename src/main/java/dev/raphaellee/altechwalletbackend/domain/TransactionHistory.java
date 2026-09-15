package dev.raphaellee.altechwalletbackend.domain;

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
