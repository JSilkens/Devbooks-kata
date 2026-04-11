package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import be.jsilkens.devbooks.shopping.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCartItemQuantityUseCaseTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;

    @Test
    @DisplayName("Should update quantity and save cart")
    void shouldUpdateQuantity() {
        // GIVEN
        var isbnValue = "9780132350884";
        var book = Book.builder()
                .withIsbn(new Isbn13(isbnValue))
                .withTitle("Clean Code")
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        var cart = ShoppingCart.empty();
        cart.addBook(book);
        when(cartRepository.get()).thenReturn(cart);

        // WHEN
        var result = updateCartItemQuantityUseCase.execute(isbnValue, 3);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        var updatedCart = ((Outcome.Success<ShoppingCart>) result).getValue();
        assertThat(updatedCart.getItems().getFirst().getQuantity()).isEqualTo(3);
        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("Should remove book when quantity is zero")
    void shouldRemoveBookWhenQuantityZero() {
        // GIVEN
        var isbnValue = "9780132350884";
        var book = Book.builder()
                .withIsbn(new Isbn13(isbnValue))
                .withTitle("Clean Code")
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        var cart = ShoppingCart.empty();
        cart.addBook(book);
        when(cartRepository.get()).thenReturn(cart);

        // WHEN
        var result = updateCartItemQuantityUseCase.execute(isbnValue, 0);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<ShoppingCart>) result).getValue().getItems()).isEmpty();
        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("Should return failure when quantity is negative")
    void shouldReturnFailureWhenQuantityNegative() {
        // GIVEN & WHEN
        var result = updateCartItemQuantityUseCase.execute("9780132350884", -1);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<ShoppingCart>) result).getMessage()).isEqualTo("Quantity cannot be negative");
        verifyNoInteractions(cartRepository);
    }

    @Test
    @DisplayName("Should return failure when book not in cart")
    void shouldReturnFailureWhenBookNotInCart() {
        // GIVEN
        when(cartRepository.get()).thenReturn(ShoppingCart.empty());

        // WHEN
        var result = updateCartItemQuantityUseCase.execute("9780132350884", 2);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<ShoppingCart>) result).getMessage()).isEqualTo("Book not found in cart");
        verify(cartRepository, never()).save(any());
    }
}
