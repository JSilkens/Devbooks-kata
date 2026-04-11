package be.jsilkens.devbooks.shopping.domain.money;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class MoneyTest {

    @Test
    @DisplayName("Should round amount to 2 decimal places using HALF_UP")
    void shouldRoundToTwoDecimalPlaces() {
        // Given
        var unroundedAmount = new BigDecimal("12.3456");
        
        // When
        var money = new Money(unroundedAmount, Currency.EUR);
        
        // Then
        assertThat(money.amount()).isEqualByComparingTo("12.35");
        assertThat(money.amount().scale()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should maintain 2 decimal places for whole numbers")
    void shouldMaintainTwoDecimalsForWholeNumbers() {
        // Given
        var wholeAmount = new BigDecimal("10");
        
        // When
        var money = new Money(wholeAmount, Currency.EUR);
        
        // Then
        assertThat(money.amount()).isEqualByComparingTo("10.00");
        assertThat(money.amount().scale()).isEqualTo(2);
    }
}
