package dev.raphaellee.altechwalletbackend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerWallet {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID walletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_username", nullable = false, updatable = false)
    private Player owner;

    @Embedded
    private MoneyWrapper balance;

    @Version
    private long version;
}
