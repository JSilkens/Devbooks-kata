package be.jsilkens.devbooks.shopping.api.mapper;

import be.jsilkens.devbooks.shopping.domain.BookListItem;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static be.jsilkens.devbooks.shopping.api.testdata.BookTestData.exampleBook;
import static org.assertj.core.api.Assertions.assertThat;

class BookApiMapperTest {

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

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
        assertThat(result.getPrice().getValue()).isEqualByComparingTo(book.getPrice().amount());
        assertThat(result.getPrice().getValue().toPlainString()).endsWith(".00");
        assertThat(result.getPrice().getCurrency()).isEqualTo(book.getPrice().currency().name());
    }

    @Test
    void givenBookListItem_whenMap_thenMappedToBookListItemDtoWithFullUrl() {
        //Given
        var request = new MockHttpServletRequest();
        request.setServerName("example.com");
        request.setServerPort(443);
        request.setScheme("https");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        var bookListItem = new BookListItem(
                "Clean Code",
                "Robert Martin",
                new Isbn13("9780135398579"),
                new Money(new BigDecimal("50.00"), Currency.EUR)
        );

        //When
        var result = BookApiMapper.map(bookListItem);

        //Then
        assertThat(result.getTitle()).isEqualTo(bookListItem.title());
        assertThat(result.getAuthor()).isEqualTo(bookListItem.author());
        assertThat(result.getPrice().getValue()).isEqualByComparingTo("50.00");
        assertThat(result.getPrice().getValue().toPlainString()).isEqualTo("50.00");
        assertThat(result.getDetailsLink()).isEqualTo("https://example.com/api/book/9780135398579");
    }
}
