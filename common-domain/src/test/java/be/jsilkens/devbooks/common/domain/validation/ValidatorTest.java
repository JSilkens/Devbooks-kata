package be.jsilkens.devbooks.common.domain.validation;

import be.jsilkens.devbooks.common.domain.validation.rules.DummyRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidatorTest {

    @Test
    @DisplayName("Given an dummy validator with one rule, when validated, then it should do validation")
    void givenItemValidatorWithOneRule_whenValidated_thenDoValidation() {
        // GIVEN
        var foo = "";
        var validator = new Validator<String>().addRule(new DummyRule());

        // WHEN
        Outcome<String> outcome = validator.validate(foo);

        // THEN
        assertThat(outcome)
                .isInstanceOf(Outcome.Failure.class)
                .extracting(failure -> ((Outcome.Failure<String>) failure).getMessage())
                .isEqualTo("String cannot be null or empty");
    }

    @Test
    @DisplayName("Given an validator with no validation rules, when validation is called, then throw exception")
    void givenValidatorWithNoRules_whenValidationCalled_thenThrowException() {
        // GIVEN
        var foo = "Some text here";
        var validator = new Validator<>();

        // WHEN & THEN
        assertThatThrownBy(() -> validator.validate(foo))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No validation rules have been added");
    }

    @Test
    @DisplayName("Given an validator with null as rule, when validation is called, then throw exception")
    void givenValidatorWithNullRule_whenValidationCalled_thenThrowException() {
        // GIVEN
        var validator = new Validator<>();

        // WHEN & THEN
        assertThatThrownBy(() -> validator.addRule(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Validation rule cannot be null");
    }

}