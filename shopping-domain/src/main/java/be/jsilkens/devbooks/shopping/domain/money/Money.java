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
}
