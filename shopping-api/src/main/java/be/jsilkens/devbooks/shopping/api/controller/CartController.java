package be.jsilkens.devbooks.shopping.api.controller;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.api.mapper.CartApiMapper;
import be.jsilkens.devbooks.shopping.api.model.AddCartItemRequestDTO;
import be.jsilkens.devbooks.shopping.api.model.UpdateCartItemRequestDTO;
import be.jsilkens.devbooks.shopping.api.model.ViewCartResponseDTO;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.usecase.AddBookToCartUseCase;
import be.jsilkens.devbooks.shopping.usecase.RemoveBookFromCartUseCase;
import be.jsilkens.devbooks.shopping.usecase.UpdateCartItemQuantityUseCase;
import be.jsilkens.devbooks.shopping.usecase.ViewCartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class CartController implements CartApi {

    private final AddBookToCartUseCase addBookToCartUseCase;
    private final RemoveBookFromCartUseCase removeBookFromCartUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final ViewCartUseCase viewCartUseCase;

    @Override
    public ResponseEntity<ViewCartResponseDTO> addBookToCart(AddCartItemRequestDTO addCartItemRequestDTO) {
        var result = addBookToCartUseCase.execute(addCartItemRequestDTO.getIsbn());

        checkNotFoundFailure(result);

        return ResponseEntity.ok(CartApiMapper.map(((Outcome.Success<ShoppingCart>) result).getValue()));
    }

    @Override
    public ResponseEntity<ViewCartResponseDTO> removeBookFromCart(String isbn) {
        var result = removeBookFromCartUseCase.execute(isbn);

        checkNotFoundFailure(result);

        return ResponseEntity.ok(CartApiMapper.map(((Outcome.Success<ShoppingCart>) result).getValue()));
    }

    @Override
    public ResponseEntity<ViewCartResponseDTO> updateCartItemQuantity(String isbn, UpdateCartItemRequestDTO updateCartItemRequestDTO) {
        var result = updateCartItemQuantityUseCase.execute(isbn, updateCartItemRequestDTO.getQuantity());

        checkNotFoundFailure(result);

        return ResponseEntity.ok(CartApiMapper.map(((Outcome.Success<ShoppingCart>) result).getValue()));
    }

    @Override
    public ResponseEntity<ViewCartResponseDTO> getCart() {
        var cart = viewCartUseCase.execute();
        return ResponseEntity.ok(CartApiMapper.map(cart));
    }

    private static void checkNotFoundFailure(Outcome<ShoppingCart> result) {
        if (result instanceof Outcome.Failure<ShoppingCart> failure) {
            if (failure.getMessage().contains("not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, failure.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, failure.getMessage());
        }
    }
}
