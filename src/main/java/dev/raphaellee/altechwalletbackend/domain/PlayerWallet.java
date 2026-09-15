package dev.raphaellee.altechwalletbackend.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Embedded
    private MoneyWrapper balance;

    @Version
    private long version;
}
