package be.jsilkens.devbooks.common.domain.validation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public sealed interface Outcome<T> permits Outcome.Success, Outcome.Failure {
    <R> Outcome<R> map(Function<T, R> mapper);

    <R> Outcome<R> flatMap(Function<T, Outcome<R>> mapper);

    Optional<T> toOptional();


    static <T> Outcome<T> merge(T value, Outcome<?>... outcomes) {
        List<String> errors = Arrays.stream(outcomes)
                .filter(o -> o instanceof Failure)
                .map(o -> ((Failure<?>) o).getMessage())
                .toList();

        if (errors.isEmpty()) {
            return new Success<>(value);
        } else {
            return new Failure<>(String.join(", ", errors));
        }
    }

    @ToString
    @EqualsAndHashCode
    @Getter
    final class Success<T> implements Outcome<T> {
        private final T value;

        public Success(T value) {
            if (Objects.isNull(value)) {
                throw new IllegalArgumentException("Success value cannot be null");
            }
            this.value = value;
        }

        @Override
        public <R> Outcome<R> map(Function<T, R> mapper) {
            return new Success<>(mapper.apply(value));
        }

        @Override
        public <R> Outcome<R> flatMap(Function<T, Outcome<R>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.of(value);
        }
    }

    @ToString
    @EqualsAndHashCode
    @Getter
    @lombok.extern.slf4j.Slf4j
    final class Failure<T> implements Outcome<T> {
        private final String message;

        public Failure(String message) {
            log.error("Validation failed: {}", message);
            this.message = message;
        }

        @Override
        public <R> Outcome<R> map(Function<T, R> mapper) {
            return new Failure<>(message);
        }

        @Override
        public <R> Outcome<R> flatMap(Function<T, Outcome<R>> mapper) {
            return new Failure<>(message);
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }
    }
}