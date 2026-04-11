package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.IntegrationTestBase;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Import(BookPersistenceFacade.class)
class BookPersistenceFacadeIT extends IntegrationTestBase {

    @Autowired
    private BookPersistenceFacade bookPersistenceFacade;

    @Test
    @DisplayName("Given an ISBN of an existing book, when finding by ISBN, then it should return the book")
    void givenExistingIsbn_whenFindByIsbn_thenReturnsBook() {
        // Given
        var isbn = new Isbn13("9780132350884");

        // When
        var actual = bookPersistenceFacade.findByIsbn(isbn);

        // Then
        assertThat(actual).isPresent();
        assertThat(actual.get().getTitle()).isEqualTo("Clean Code");
        assertThat(actual.get().getAuthor()).isEqualTo("Robert Martin");
        assertThat(actual.get().getPublicationYear()).isEqualTo(2008);
        assertThat(actual.get().getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(actual.get().getPrice().currency()).isEqualTo(Currency.EUR);
        assertThat(actual.get().getIsbn()).isEqualTo(isbn);
    }

    @Test
    @DisplayName("Given an ISBN of a non-existing book, when finding by ISBN, then it should return empty")
    void givenNonExistingIsbn_whenFindByIsbn_thenReturnsEmpty() {
        // Given
        var isbn = new Isbn13("9780000000000");

        // When
        var actual = bookPersistenceFacade.findByIsbn(isbn);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("When finding all books paginated, then it should return a paginated result")
    void whenFindAll_thenReturnsPaginatedResult() {
        // When
        var actual = bookPersistenceFacade.findAll(1, 10);

        // Then
        assertThat(actual.items()).isNotEmpty();
        assertThat(actual.metadata().currentPage()).isEqualTo(1);
        assertThat(actual.metadata().pageSize()).isEqualTo(10);
        assertThat(actual.metadata().totalItems()).isGreaterThanOrEqualTo(1);
    }
}
