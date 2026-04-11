package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class UpdateCartItemQuantityUseCase {

    private final CartRepository cartRepository;

    public Outcome<ShoppingCart> execute(String isbnValue, int quantity) {
        if (quantity < 0) {
            return new Outcome.Failure<>("Quantity cannot be negative");
        }

        return Isbn13.builder()
                .withValue(isbnValue)
                .build()
                .validate()
                .flatMap(isbn -> {
                    var cart = cartRepository.get();
                    if (!cart.updateQuantity(isbn, quantity)) {
                        return new Outcome.Failure<>("Book not found in cart");
                    }
                    cartRepository.save(cart);
                    return new Outcome.Success<>(cart);
                });
    }
}
