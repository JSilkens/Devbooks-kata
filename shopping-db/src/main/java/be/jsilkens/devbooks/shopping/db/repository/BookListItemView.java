package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.domain.money.Currency;
import java.math.BigDecimal;

public interface BookListItemView {
    String getTitle();
    String getAuthor();
    String getIsbn();
    BigDecimal getPriceAmount();
    Currency getPriceCurrency();
}
