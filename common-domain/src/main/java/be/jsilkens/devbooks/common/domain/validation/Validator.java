package be.jsilkens.devbooks.common.domain.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Validator<T> {
    private final List<ValidationRule<T>> rules = new ArrayList<>();

    public Validator<T> addRule(ValidationRule<T> rule) {
        if (Objects.isNull(rule)) {
            throw new IllegalArgumentException("Validation rule cannot be null");
        }
        rules.add(rule);
        return this;
    }

    public Outcome<T> validate(T item) {
        if (rules.isEmpty()) {
            throw new IllegalStateException("No validation rules have been added");
        }
        List<String> errors = rules.stream()
                .flatMap(rule -> {
                    List<String> ruleErrors = new ArrayList<>();
                    rule.validate(item, ruleErrors);
                    return ruleErrors.stream();
                })
                .toList();

        if (errors.isEmpty()) {
            return new Outcome.Success<>(item);
        } else {
            return new Outcome.Failure<>(String.join(", ", errors));
        }
    }
}
