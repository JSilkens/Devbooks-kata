package be.jsilkens.devbooks.shopping.db.mapper;

import be.jsilkens.devbooks.shopping.db.entity.CartDb;
import be.jsilkens.devbooks.shopping.db.entity.CartItemDb;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.CartItem;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartMapper {

    public static ShoppingCart map(CartDb cartDb, Map<Isbn13, Book> bookMap) {
        var items = cartDb.getItems().stream()
                .map(itemDb -> {
                    var isbn = Isbn13.builder().withValue(itemDb.getBookIsbn()).build();
                    var book = bookMap.get(isbn);
                    if (book == null) {
                        throw new IllegalStateException("Book with ISBN " + isbn.value() + " not found but referenced in cart");
                    }
                    return map(itemDb, book);
                })
                .collect(Collectors.toList());

        return ShoppingCart.builder()
                .withItems(items)
                .build();
    }

    private static CartItem map(CartItemDb itemDb, Book book) {
        return CartItem.builder()
                .withBook(book)
                .withQuantity(itemDb.getQuantity())
                .build();
    }

    public static List<CartItemDb> mapItems(ShoppingCart cart, CartDb cartDb) {
        return cart.getItems().stream()
                .map(item -> CartItemDb.builder()
                        .cart(cartDb)
                        .bookIsbn(item.getBook().getIsbn().value())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}
