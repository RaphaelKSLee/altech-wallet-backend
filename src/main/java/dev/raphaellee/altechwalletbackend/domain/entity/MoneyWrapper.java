package dev.raphaellee.altechwalletbackend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MoneyWrapper {

    @NotNull
    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    // Target fiat, crypto may need scale = 18 for ETH?
    // Joda money doesn't like non-fiats anyway
    @NotNull
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    public MoneyWrapper(Money money) {
        this.currencyCode = money.getCurrencyUnit().getCode();
        this.amount = money.getAmount();
    }

    public Money toMoney() {
        BigDecimal normalizedAmount = amount.stripTrailingZeros();
        return Money.of(
                CurrencyUnit.of(currencyCode),
                normalizedAmount,
                RoundingMode.HALF_EVEN);
    }
}
