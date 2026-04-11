package be.jsilkens.devbooks.shopping.domain;

import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCart {
    private final List<CartItem> items;

    public static ShoppingCart empty() {
        return new ShoppingCart(new ArrayList<>());
    }

    public void addBook(Book book) {
        items.stream()
                .filter(item -> item.getBook().getIsbn().equals(book.getIsbn()))
                .findFirst()
                .ifPresentOrElse(
                        CartItem::incrementQuantity,
                        () -> items.add(CartItem.builder()
                                .withBook(book)
                                .withQuantity(1)
                                .build())
                );
    }

    public boolean removeBook(Isbn13 isbn) {
        return items.removeIf(item -> item.getBook().getIsbn().equals(isbn));
    }

    public boolean updateQuantity(Isbn13 isbn, int quantity) {
        if (quantity == 0) {
            return removeBook(isbn);
        }
        return items.stream()
                .filter(item -> item.getBook().getIsbn().equals(isbn))
                .findFirst()
                .map(item -> {
                    item.setQuantity(quantity);
                    return true;
                })
                .orElse(false);
    }

    public Money getTotalPrice() {
        if (items.isEmpty()) {
            return Money.zero(Currency.EUR);
        }
        return items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(Money::add)
                .orElse(Money.zero(Currency.EUR));
    }
}
