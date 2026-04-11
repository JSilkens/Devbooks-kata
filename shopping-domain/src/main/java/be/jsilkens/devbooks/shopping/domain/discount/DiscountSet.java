package be.jsilkens.devbooks.shopping.domain.discount;

import be.jsilkens.devbooks.shopping.domain.money.Money;

import java.util.List;

public record DiscountSet(
        String description,
        List<String> booksIncluded,
        Money originalPrice,
        Money discountedPrice,
        Money savings
) {
}
