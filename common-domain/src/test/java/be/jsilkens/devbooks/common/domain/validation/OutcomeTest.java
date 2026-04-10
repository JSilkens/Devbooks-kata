package be.jsilkens.devbooks.common.domain.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutcomeTest {

    @Test
    @DisplayName("Given a Success outcome, when map is called, it should transform the value")
    void givenSuccessOutcome_whenMap_thenTransformValue() {
        // GIVEN
        Outcome<String> outcome = new Outcome.Success<>("Hello");

        // WHEN
        Outcome<Integer> result = outcome.map(String::length);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<Integer>) result).getValue()).isEqualTo(5);
    }

    @Test
    @DisplayName("Given a Failure outcome, when map is called, it should not transform the value")
    void givenFailureOutcome_whenMap_thenDoNotTransformValue() {
        // GIVEN
        Outcome<String> outcome = new Outcome.Failure<>("Error");

        // WHEN
        Outcome<Integer> result = outcome.map(String::length);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<Integer>) result).getMessage()).isEqualTo("Error");
    }

    @Test
    @DisplayName("Given a Success outcome, when flatMap is called, it should transform to a new Outcome")
    void givenSuccessOutcome_whenFlatMap_thenTransformToNewOutcome() {
        // GIVEN
        Outcome<String> outcome = new Outcome.Success<>("Hello");

        // WHEN
        Outcome<Integer> result = outcome.flatMap(s -> new Outcome.Success<>(s.length()));

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<Integer>) result).getValue()).isEqualTo(5);
    }

    @Test
    @DisplayName("Given a Failure outcome, when flatMap is called, it should not transform the value")
    void givenFailureOutcome_whenFlatMap_thenDoNotTransformValue() {
        // GIVEN
        Outcome<String> outcome = new Outcome.Failure<>("Error");

        // WHEN
        Outcome<Integer> result = outcome.flatMap(s -> new Outcome.Success<>(s.length()));

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<Integer>) result).getMessage()).isEqualTo("Error");
    }

    @Test
    @DisplayName("Given a Success outcome, when toOptional is called, it should return a present Optional")
    void givenSuccessOutcome_whenToOptional_thenReturnPresentOptional() {
        // GIVEN
        Outcome<String> outcome = new Outcome.Success<>("Hello");

        // WHEN
        Optional<String> optional = outcome.toOptional();

        // THEN
        assertThat(optional).isPresent().contains("Hello");
    }

    @Test
    @DisplayName("Given a Failure outcome, when toOptional is called, it should return an empty Optional")
    void givenFailureOutcome_whenToOptional_thenReturnEmptyOptional() {
        // GIVEN
        Outcome<String> outcome = new Outcome.Failure<>("Error");

        // WHEN
        Optional<String> optional = outcome.toOptional();

        // THEN
        assertThat(optional).isEmpty();
    }

    @Test
    @DisplayName("Given a null value, when Success is constructed, it should throw an exception")
    void givenNullValue_whenSuccessConstructed_thenThrowException() {
        // GIVEN & WHEN & THEN
        assertThatThrownBy(() -> new Outcome.Success<>(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Success value cannot be null");
    }

}