package be.jsilkens.devbooks.shopping.domain.discount;

import be.jsilkens.devbooks.shopping.domain.CartItem;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscountCalculator {

    private static final BigDecimal BOOK_PRICE = new BigDecimal("50.00");

    private static final Map<Integer, BigDecimal> DISCOUNT_RATES = Map.of(
            2, new BigDecimal("0.05"),
            3, new BigDecimal("0.10"),
            4, new BigDecimal("0.20"),
            5, new BigDecimal("0.25")
    );

    public static DiscountResult calculate(ShoppingCart cart) {
        if (cart.getItems().isEmpty()) {
            return new DiscountResult(Money.zero(Currency.EUR), Money.zero(Currency.EUR), List.of());
        }

        var quantities = new LinkedHashMap<String, Integer>();
        for (CartItem item : cart.getItems()) {
            quantities.put(item.getBook().getTitle(), item.getQuantity());
        }

        var setCounts = greedySets(new LinkedHashMap<>(quantities));

        optimize(setCounts);

        var discountSets = buildDiscountSets(quantities, setCounts);

        var totalPrice = Money.zero(Currency.EUR);
        var totalSavings = Money.zero(Currency.EUR);

        for (DiscountSet ds : discountSets) {
            totalPrice = totalPrice.add(ds.discountedPrice());
            totalSavings = totalSavings.add(ds.savings());
        }

        // Add remaining single books (no discount, not in breakdown)
        int totalInSets = discountSets.stream().mapToInt(ds -> ds.booksIncluded().size()).sum();
        int totalBooks = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
        int singles = totalBooks - totalInSets;
        if (singles > 0) {
            totalPrice = totalPrice.add(new Money(BOOK_PRICE.multiply(BigDecimal.valueOf(singles)), Currency.EUR));
        }

        return new DiscountResult(totalPrice, totalSavings, discountSets);
    }

    private static Map<Integer, Integer> greedySets(Map<String, Integer> quantities) {
        var setCounts = new LinkedHashMap<Integer, Integer>();
        while (true) {
            int distinctAvailable = (int) quantities.values().stream().filter(q -> q > 0).count();
            if (distinctAvailable < 2) break;

            // Take one copy of each distinct book that still has copies
            int setSize = distinctAvailable;
            for (var entry : quantities.entrySet()) {
                if (entry.getValue() > 0) {
                    entry.setValue(entry.getValue() - 1);
                }
            }
            setCounts.merge(setSize, 1, Integer::sum);
        }
        return setCounts;
    }

    private static void optimize(Map<Integer, Integer> setCounts) {
        // Convert (5+3) pairs into (4+4) pairs — always cheaper
        int fives = setCounts.getOrDefault(5, 0);
        int threes = setCounts.getOrDefault(3, 0);
        int pairs = Math.min(fives, threes);
        if (pairs > 0) {
            setCounts.put(5, fives - pairs);
            setCounts.put(3, threes - pairs);
            setCounts.merge(4, pairs * 2, Integer::sum);
            setCounts.values().removeIf(v -> v == 0);
        }
    }

    private static List<DiscountSet> buildDiscountSets(Map<String, Integer> originalQuantities, Map<Integer, Integer> setCounts) {
        var remaining = new LinkedHashMap<>(originalQuantities);
        var result = new ArrayList<DiscountSet>();

        var sortedSizes = new ArrayList<>(setCounts.keySet());
        sortedSizes.sort(Comparator.reverseOrder());

        for (int size : sortedSizes) {
            int count = setCounts.get(size);
            for (int i = 0; i < count; i++) {
                var titles = new ArrayList<String>();
                for (var entry : remaining.entrySet()) {
                    if (titles.size() == size) break;
                    if (entry.getValue() > 0) {
                        titles.add(entry.getKey());
                        entry.setValue(entry.getValue() - 1);
                    }
                }
                result.add(createDiscountSet(size, titles));
            }
        }

        return result;
    }

    private static DiscountSet createDiscountSet(int size, List<String> titles) {
        var rate = DISCOUNT_RATES.getOrDefault(size, BigDecimal.ZERO);
        var original = BOOK_PRICE.multiply(BigDecimal.valueOf(size));
        var savings = original.multiply(rate);
        var discounted = original.subtract(savings);

        return new DiscountSet(
                "Set of %d unique books (%s%% discount)".formatted(size, rate.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString()),
                titles,
                new Money(original, Currency.EUR),
                new Money(discounted, Currency.EUR),
                new Money(savings, Currency.EUR)
        );
    }

}
