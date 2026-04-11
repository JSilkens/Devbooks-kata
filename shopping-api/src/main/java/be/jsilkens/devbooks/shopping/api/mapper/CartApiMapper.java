package be.jsilkens.devbooks.shopping.api.mapper;

import be.jsilkens.devbooks.shopping.api.model.CartItemResponseDTO;
import be.jsilkens.devbooks.shopping.api.model.Price;
import be.jsilkens.devbooks.shopping.api.model.ShoppingCartResponseDTO;
import be.jsilkens.devbooks.shopping.domain.CartItem;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartApiMapper {

    public static ShoppingCartResponseDTO map(ShoppingCart cart) {
        var responseDTO = new ShoppingCartResponseDTO();
        responseDTO.setBooks(cart.getItems().stream()
                .map(CartApiMapper::map)
                .toList());
        responseDTO.setTotalPrice(mapPrice(cart.getTotalPrice().amount(), cart.getTotalPrice().currency().name()));
        return responseDTO;
    }

    public static CartItemResponseDTO map(CartItem item) {
        var responseDTO = new CartItemResponseDTO();
        responseDTO.setTitle(item.getBook().getTitle());
        responseDTO.setPrice(mapPrice(item.getBook().getPrice().amount(), item.getBook().getPrice().currency().name()));
        responseDTO.setQuantity(item.getQuantity());
        responseDTO.setDetailsLink(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/book/")
                .path(item.getBook().getIsbn().value())
                .toUriString());
        return responseDTO;
    }

    private static Price mapPrice(BigDecimal amount, String currency) {
        var price = new Price();
        price.setValue(amount.setScale(2, RoundingMode.HALF_UP));
        price.setCurrency(currency);
        return price;
    }
}
