package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.event.DomainEventPublisher;
import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.event.BookAddedToCartEvent;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import be.jsilkens.devbooks.shopping.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddBookToCartUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private AddBookToCartUseCase addBookToCartUseCase;

    @Test
    @DisplayName("Should publish BookAddedToCartEvent when book is added")
    void shouldPublishEventWhenBookAdded() {
        // GIVEN
        var isbnValue = "9780132350884";
        var isbn = new Isbn13(isbnValue);
        var book = Book.builder()
                .withIsbn(isbn)
                .withTitle("Clean Code")
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(cartRepository.get()).thenReturn(ShoppingCart.empty());

        // WHEN
        var result = addBookToCartUseCase.execute(isbnValue);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        verify(cartRepository).save(any());
        verify(eventPublisher).publish(any(BookAddedToCartEvent.class));
    }
}
