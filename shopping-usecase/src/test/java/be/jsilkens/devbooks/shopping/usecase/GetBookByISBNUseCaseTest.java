package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookByISBNUseCaseTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private GetBookByISBNUseCase useCase;

    @Test
    @DisplayName("Given a valid ISBN, when book exists, then it should return a Success with Optional of the book")
    void givenValidIsbn_whenBookExists_thenReturnSuccessWithOptionalOfBook() {
        // GIVEN
        String isbnValue = "9780132350884";
        Book book = Book.builder()
                .withTitle("Clean Code")
                .withIsbn(new Isbn13(isbnValue))
                .build();
        when(bookRepository.findByIsbn(any(Isbn13.class))).thenReturn(Optional.of(book));

        // WHEN
        Outcome<Optional<Book>> result = useCase.execute(isbnValue);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<Optional<Book>>) result).getValue()).contains(book);
    }

    @Test
    @DisplayName("Given a valid ISBN, when book does not exist, then it should return a Success with an empty Optional")
    void givenValidIsbn_whenBookDoesNotExist_thenReturnSuccessWithEmptyOptional() {
        // GIVEN
        String isbnValue = "9780132350884";
        when(bookRepository.findByIsbn(any(Isbn13.class))).thenReturn(Optional.empty());

        // WHEN
        Outcome<Optional<Book>> result = useCase.execute(isbnValue);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Success.class);
        assertThat(((Outcome.Success<Optional<Book>>) result).getValue()).isEmpty();
    }

    @Test
    @DisplayName("Given an invalid ISBN format, when executed, then it should return a Failure with 'Invalid ISBN13'")
    void givenInvalidIsbnFormat_whenExecuted_thenReturnFailure() {
        // GIVEN
        String invalidIsbn = "123";

        // WHEN
        Outcome<Optional<Book>> result = useCase.execute(invalidIsbn);

        // THEN
        assertThat(result).isInstanceOf(Outcome.Failure.class);
        assertThat(((Outcome.Failure<Optional<Book>>) result).getMessage()).isEqualTo("Invalid ISBN13");
    }
}
