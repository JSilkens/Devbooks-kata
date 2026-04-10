package be.jsilkens.devbooks.shopping.api.mapper;

import org.junit.jupiter.api.Test;

import static be.jsilkens.devbooks.shopping.api.testdata.BookTestData.exampleBook;
import static org.assertj.core.api.Assertions.assertThat;

class BookApiMapperTest {

    @Test
    void givenBookDomain_whenMap_thenMappedToBookDto() {
        //Given
        var book = exampleBook();

        //When
        var result = BookApiMapper.map(book);

        //Then
        assertThat(result.getTitle()).isEqualTo(book.getTitle());
        assertThat(result.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(result.getIsbn()).isEqualTo(book.getIsbn().value());
        assertThat(result.getPublicationYear()).isEqualTo(book.getPublicationYear());
        assertThat(result.getPrice().getValue()).isEqualTo(book.getPrice().amount().doubleValue());
        assertThat(result.getPrice().getCurrency()).isEqualTo(book.getPrice().currency().name());
    }
}
