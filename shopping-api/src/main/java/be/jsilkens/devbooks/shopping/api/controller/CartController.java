package be.jsilkens.devbooks.shopping.api.controller;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.api.mapper.CartApiMapper;
import be.jsilkens.devbooks.shopping.api.model.AddCartItemRequestDTO;
import be.jsilkens.devbooks.shopping.api.model.ShoppingCartResponseDTO;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.usecase.AddBookToCartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class CartController implements CartApi {

    private final AddBookToCartUseCase addBookToCartUseCase;

    @Override
    public ResponseEntity<ShoppingCartResponseDTO> addBookToCart(AddCartItemRequestDTO addCartItemRequestDTO) {
        var result = addBookToCartUseCase.execute(addCartItemRequestDTO.getIsbn());

        if (result instanceof Outcome.Failure<ShoppingCart> failure) {
            if (failure.getMessage().contains("not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, failure.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, failure.getMessage());
        }

        return ResponseEntity.ok(CartApiMapper.map(((Outcome.Success<ShoppingCart>) result).getValue()));
    }
}
