package be.jsilkens.devbooks.shopping.domain;

import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem {
    private final Book book;
    private Integer quantity;

    public void incrementQuantity() {
        this.quantity++;
    }

    public Money getTotalPrice() {
        return book.getPrice().multiply(quantity);
    }
}
