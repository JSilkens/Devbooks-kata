package be.jsilkens.devbooks.shopping.domain.money;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder(setterPrefix = "with")
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount != null) {
            amount = amount.setScale(2, RoundingMode.HALF_UP);
        }
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int quantity) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }
}
