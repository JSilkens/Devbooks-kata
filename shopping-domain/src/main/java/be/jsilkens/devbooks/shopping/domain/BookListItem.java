package be.jsilkens.devbooks.shopping.domain;

import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Money;

public record BookListItem(
        String title,
        String author,
        Isbn13 isbn,
        Money price
) {}
