package be.jsilkens.devbooks.shopping.domain.identifier;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class Isbn13Test {

    @ParameterizedTest
    @ValueSource(strings = {"978-0134685991", "9780134685991", "978-3-16-148410-0", "9783161484100"})
    @DisplayName("Given a valid ISBN13, when validated, then it should return success")
    void givenValidIsbn13_whenValidated_thenSuccess(String value) {
        // GIVEN
        var isbn = new Isbn13(value);

        // WHEN
        var outcome = isbn.validate();

        // THEN
        assertThat(outcome).isInstanceOf(Outcome.Success.class);
        assertThat(outcome.toOptional()).isPresent().contains(isbn);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "123456789012", "12345678901234", "978-0134685992", "invalid-isbn"})
    @DisplayName("Given an invalid ISBN13, when validated, then it should return failure")
    void givenInvalidIsbn13_whenValidated_thenFailure(String value) {
        // GIVEN
        var isbn = new Isbn13(value);

        // WHEN
        var outcome = isbn.validate();

        // THEN
        assertThat(outcome).isInstanceOf(Outcome.Failure.class);
        var failure = (Outcome.Failure<Isbn13>) outcome;
        assertThat(failure.getMessage()).isNotBlank();
    }
}
