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
class RemoveBookFromCartUseCaseTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private RemoveBookFromCartUseCase removeBookFromCartUseCase;

    @Test
    @DisplayName("Should remove book from cart and save")
    void shouldRemoveBookFromCart() {
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
        var result = removeBookFromCartUseCase.execute(isbnValue);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<ShoppingCart>) result).getValue().getItems()).isEmpty();
        verify(cartRepository).save(any());
    }

    @Test
    @DisplayName("Should return failure when book not in cart")
    void shouldReturnFailureWhenBookNotInCart() {
        // GIVEN
        when(cartRepository.get()).thenReturn(ShoppingCart.empty());

        // WHEN
        var result = removeBookFromCartUseCase.execute("9780132350884");

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<ShoppingCart>) result).getMessage()).isEqualTo("Book not found in cart");
        verify(cartRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return failure when ISBN is invalid")
    void shouldReturnFailureWhenIsbnInvalid() {
        // GIVEN & WHEN
        var result = removeBookFromCartUseCase.execute("INVALID_ISBN");

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        verifyNoInteractions(cartRepository);
    }
}
