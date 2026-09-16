package dev.raphaellee.altechwalletbackend.domain.exception;

import org.joda.money.Money;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Money toAllocate, Money available) {
        super("Attempting to allocate: " + toAllocate.toString() +
                ", but only has available: " + available.toString());
    }


}
