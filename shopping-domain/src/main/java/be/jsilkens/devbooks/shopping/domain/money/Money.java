package be.jsilkens.devbooks.shopping.domain.money;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(setterPrefix = "with")
public record Money(BigDecimal amount, Currency currency) {
}
