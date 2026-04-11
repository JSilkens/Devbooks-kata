package be.jsilkens.devbooks.shopping.api.mapper;

import be.jsilkens.devbooks.shopping.api.model.DiscountSetDTO;
import be.jsilkens.devbooks.shopping.api.model.ViewCartItemResponseDTO;
import be.jsilkens.devbooks.shopping.api.model.ViewCartResponseDTO;
import be.jsilkens.devbooks.shopping.domain.CartItem;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.discount.DiscountCalculator;
import be.jsilkens.devbooks.shopping.domain.discount.DiscountSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartApiMapper {

    public static ViewCartResponseDTO map(ShoppingCart cart) {
        var discountResult = DiscountCalculator.calculate(cart);
        return ViewCartResponseDTO.builder()
                .books(cart.getItems().stream()
                        .map(CartApiMapper::mapToViewCartItemResponseDTO)
                        .toList())
                .totalPrice(discountResult.totalPrice().amount().setScale(2, RoundingMode.HALF_UP))
                .totalSavings(discountResult.totalSavings().amount().setScale(2, RoundingMode.HALF_UP))
                .discountBreakdown(discountResult.discountSets().stream()
                        .map(CartApiMapper::mapToDiscountSetDTO)
                        .toList())
                .build();
    }

    private static ViewCartItemResponseDTO mapToViewCartItemResponseDTO(CartItem item) {
        return ViewCartItemResponseDTO.builder()
                .title(item.getBook().getTitle())
                .priceInEur(item.getBook().getPrice().amount().setScale(2, RoundingMode.HALF_UP))
                .quantity(item.getQuantity())
                .detailsLink(ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/book/")
                        .path(item.getBook().getIsbn().value())
                        .toUriString())
                .build();
    }

    private static DiscountSetDTO mapToDiscountSetDTO(DiscountSet ds) {
        return DiscountSetDTO.builder()
                .description(ds.description())
                .booksIncluded(ds.booksIncluded())
                .originalPrice(ds.originalPrice().amount().setScale(2, RoundingMode.HALF_UP))
                .discountedPrice(ds.discountedPrice().amount().setScale(2, RoundingMode.HALF_UP))
                .savings(ds.savings().amount().setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
