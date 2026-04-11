package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.event.DomainEventPublisher;
import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.event.BookAddedToCartEvent;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import be.jsilkens.devbooks.shopping.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class AddBookToCartUseCase {

    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final DomainEventPublisher eventPublisher;

    public Outcome<ShoppingCart> execute(String isbnValue) {
        return Isbn13.builder()
                .withValue(isbnValue)
                .build()
                .validate()
                .flatMap(isbn -> bookRepository.findByIsbn(isbn)
                        .map(book -> {
                            var cart = cartRepository.get();
                            cart.addBook(book);
                            cartRepository.save(cart);

                            eventPublisher.publish(new BookAddedToCartEvent(book.getIsbn().value()));

                            return (Outcome<ShoppingCart>) new Outcome.Success<>(cart);
                        })
                        .orElseGet(() -> new Outcome.Failure<>("Book not found")));
    }
}
