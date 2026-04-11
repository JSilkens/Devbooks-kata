package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.IntegrationTestBase;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CartPersistenceFacadeIT extends IntegrationTestBase {

    @Autowired
    private CartPersistenceFacade cartPersistenceFacade;

    @Test
    @DisplayName("Should save and retrieve a shopping cart")
    void shouldSaveAndRetrieveCart() {
        // GIVEN
        var book = Book.builder()
                .withTitle("Clean Code")
                .withAuthor("Robert Martin")
                .withPublicationYear(2008)
                .withIsbn(new Isbn13("9780132350884"))
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        var cart = ShoppingCart.empty();
        cart.addBook(book);

        // WHEN
        cartPersistenceFacade.save(cart);
        var retrievedCart = cartPersistenceFacade.get();

        // THEN
        assertThat(retrievedCart).isNotNull();
        assertThat(retrievedCart.getItems()).hasSize(1);
        assertThat(retrievedCart.getItems().get(0).getBook().getTitle()).isEqualTo("Clean Code");
        assertThat(retrievedCart.getItems().get(0).getQuantity()).isEqualTo(1);
        assertThat(retrievedCart.getTotalPrice().amount()).isEqualByComparingTo("50.00");
    }
}
