package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ViewCartUseCase {

    private final CartRepository cartRepository;

    public ShoppingCart execute() {
        return cartRepository.get();
    }
}
