package be.jsilkens.devbooks.common.domain.validation.rules;

import be.jsilkens.devbooks.common.domain.validation.ValidationRule;

import java.util.List;

public class DummyRule implements ValidationRule<String> {
    @Override
    public void validate(String string, List<String> errors) {
        if (string == null || string.trim().isEmpty()) {
            errors.add("String cannot be null or empty");
        }
    }
}
