package be.jsilkens.devbooks.common.domain.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutcomeTest {

    @Test
    @DisplayName("Given a Success outcome, when map is called, it should transform the value")
    void givenSuccessOutcome_whenMap_thenTransformValue() {
        // GIVEN
        var outcome = new Outcome.Success<>("Hello");

        // WHEN
        var result = outcome.map(String::length);

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
        var outcome = new Outcome.Success<>("Hello");

        // WHEN
        var result = outcome.flatMap(s -> new Outcome.Success<>(s.length()));

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
        var outcome = new Outcome.Success<>("Hello");

        // WHEN
        var optional = outcome.toOptional();

        // THEN
        assertThat(optional).isPresent().contains("Hello");
    }

    @Test
    @DisplayName("Given a Failure outcome, when toOptional is called, it should return an empty Optional")
    void givenFailureOutcome_whenToOptional_thenReturnEmptyOptional() {
        // GIVEN
        var outcome = new Outcome.Failure<>("Error");

        // WHEN
        var optional = outcome.toOptional();

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

    @Test
    @DisplayName("Given multiple Success outcomes, when merged with a value, it should return a Success containing that value")
    void givenSuccessOutcomes_whenMerge_thenReturnSuccessWithValue() {
        // GIVEN
        var o1 = new Outcome.Success<>("A");
        var o2 = new Outcome.Success<>(1);

        // WHEN
        var result = Outcome.merge("Result", o1, o2);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<String>) result).getValue()).isEqualTo("Result");
    }

    @Test
    @DisplayName("Given some Failure outcomes, when merged, it should return a Failure with aggregated messages")
    void givenFailureOutcomes_whenMerge_thenReturnFailureWithAggregatedMessages() {
        // GIVEN
        var o1 = new Outcome.Success<>("A");
        var o2 = new Outcome.Failure<>("Error 1");
        var o3 = new Outcome.Failure<>("Error 2");

        // WHEN
        var result = Outcome.merge("Result", o1, o2, o3);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<String>) result).getMessage())
                .isEqualTo("Error 1, Error 2");
    }
}