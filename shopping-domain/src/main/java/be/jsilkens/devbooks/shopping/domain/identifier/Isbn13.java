package be.jsilkens.devbooks.shopping.domain.identifier;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.common.domain.validation.Validatable;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record Isbn13(String value) implements Validatable {

    private static final String ISBN13REGEX = "^\\d{13}$";

    @Override
    public Outcome<Isbn13> validate() {
        if (value == null || value.isBlank()) {
            return new Outcome.Failure<>("ISBN13 cannot be blank");
        }
        String isbn = value.replace("-", "");

        if (!isbn.matches(ISBN13REGEX)) {
            return new Outcome.Failure<>("Invalid ISBN13");
        }
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        return sum % 10 == 0 ? new Outcome.Success<>(this) : new Outcome.Failure<>("Invalid ISBN13 checksum");
    }
}
