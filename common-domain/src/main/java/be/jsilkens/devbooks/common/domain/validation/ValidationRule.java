package be.jsilkens.devbooks.common.domain.validation;

import java.util.List;

public interface ValidationRule<T> {

    void validate(T object, List<String> errors);
}
