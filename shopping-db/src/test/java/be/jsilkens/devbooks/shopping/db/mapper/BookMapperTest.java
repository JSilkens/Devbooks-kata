package be.jsilkens.devbooks.shopping.db.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static be.jsilkens.devbooks.shopping.db.testdata.BookTestData.exampleBook;
import static be.jsilkens.devbooks.shopping.db.testdata.BookTestData.exampleBookDb;
import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    @Test
    @DisplayName("Given a book domain object, when mapping, then it should map to a database entity")
    void givenBookDomain_whenMap_then_mappedToDb() {
        //Given
        var book = exampleBook();
        var expected = exampleBookDb();

        //when
        var actual = BookMapper.map(book);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Given a book database entity, when mapping, then it should map to a domain object")
    void givenBookDb_whenMap_then_mappedToDomain() {
        //Given
        var book = exampleBookDb();
        var expected = exampleBook();

        //when
        var actual = BookMapper.map(book);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}