package be.jsilkens.devbooks.shopping.domain.discount;

import be.jsilkens.devbooks.shopping.domain.money.Money;

import java.util.List;

public record DiscountResult(
        Money totalPrice,
        Money totalSavings,
        List<DiscountSet> discountSets
) {
}
