package be.jsilkens.devbooks.shopping.domain.discount;

import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountCalculatorTest {

    @Test
    @DisplayName("Should return no discounts for single book type")
    void noDiscount_singleBookType() {
        // GIVEN
        var cart = ShoppingCart.empty();
        cart.addBook(book("9780132350884", "Clean Code"));
        cart.addBook(book("9780132350884", "Clean Code"));

        // WHEN
        var result = DiscountCalculator.calculate(cart);

        // THEN
        assertThat(result.totalPrice().amount()).isEqualByComparingTo("100.00");
        assertThat(result.totalSavings().amount()).isEqualByComparingTo("0.00");
        assertThat(result.discountSets()).isEmpty();
    }

    @Test
    @DisplayName("Should apply 5% discount for 2 different books")
    void discount_twoDifferentBooks() {
        // GIVEN
        var cart = ShoppingCart.empty();
        cart.addBook(book("9780132350884", "Clean Code"));
        cart.addBook(book("9780137081073", "Clean Coder"));

        // WHEN
        var result = DiscountCalculator.calculate(cart);

        // THEN
        assertThat(result.totalPrice().amount()).isEqualByComparingTo("95.00");
        assertThat(result.totalSavings().amount()).isEqualByComparingTo("5.00");
        assertThat(result.discountSets()).hasSize(1);
        assertThat(result.discountSets().getFirst().booksIncluded()).containsExactly("Clean Code", "Clean Coder");
    }

    @Test
    @DisplayName("Should optimize 5+3 into 4+4 for maximum discount")
    void optimization_fivePlusThree_becomesFourPlusFour() {
        // GIVEN — the exact scenario from the issue
        var cart = ShoppingCart.empty();
        addCopies(cart, "9780132350884", "Clean Code", 2);
        addCopies(cart, "9780137081073", "Clean Coder", 2);
        addCopies(cart, "9780134494166", "Clean Architecture", 2);
        addCopies(cart, "9780321146533", "Test Driven Development by Example", 1);
        addCopies(cart, "9780131177055", "Working effectively with Legacy Code", 1);

        // WHEN
        var result = DiscountCalculator.calculate(cart);

        // THEN
        assertThat(result.totalPrice().amount()).isEqualByComparingTo("320.00");
        assertThat(result.totalSavings().amount()).isEqualByComparingTo("80.00");
        assertThat(result.discountSets()).hasSize(2);
        result.discountSets().forEach(ds -> {
            assertThat(ds.booksIncluded()).hasSize(4);
            assertThat(ds.originalPrice().amount()).isEqualByComparingTo("200.00");
            assertThat(ds.discountedPrice().amount()).isEqualByComparingTo("160.00");
            assertThat(ds.savings().amount()).isEqualByComparingTo("40.00");
        });
    }

    @Test
    @DisplayName("Should return empty result for empty cart")
    void emptyCart() {
        // GIVEN
        var cart = ShoppingCart.empty();

        // WHEN
        var result = DiscountCalculator.calculate(cart);

        // THEN
        assertThat(result.totalPrice().amount()).isEqualByComparingTo("0.00");
        assertThat(result.totalSavings().amount()).isEqualByComparingTo("0.00");
        assertThat(result.discountSets()).isEmpty();
    }

    @Test
    @DisplayName("Should apply 25% discount for 5 different books")
    void discount_fiveDifferentBooks() {
        // GIVEN
        var cart = ShoppingCart.empty();
        cart.addBook(book("9780132350884", "Clean Code"));
        cart.addBook(book("9780137081073", "Clean Coder"));
        cart.addBook(book("9780134494166", "Clean Architecture"));
        cart.addBook(book("9780321146533", "TDD by Example"));
        cart.addBook(book("9780131177055", "Legacy Code"));

        // WHEN
        var result = DiscountCalculator.calculate(cart);

        // THEN
        assertThat(result.totalPrice().amount()).isEqualByComparingTo("187.50");
        assertThat(result.totalSavings().amount()).isEqualByComparingTo("62.50");
        assertThat(result.discountSets()).hasSize(1);
        assertThat(result.discountSets().getFirst().description()).contains("25%");
    }

    private static Book book(String isbn, String title) {
        return Book.builder()
                .withIsbn(new Isbn13(isbn))
                .withTitle(title)
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();
    }

    private static void addCopies(ShoppingCart cart, String isbn, String title, int copies) {
        for (int i = 0; i < copies; i++) {
            cart.addBook(book(isbn, title));
        }
    }
}
