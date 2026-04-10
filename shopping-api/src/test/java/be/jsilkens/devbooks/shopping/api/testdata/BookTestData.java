package be.jsilkens.devbooks.shopping.api.testdata;

import be.jsilkens.devbooks.shopping.api.model.BookDTO;
import be.jsilkens.devbooks.shopping.api.model.Price;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookTestData {

    public static Book exampleBook() {
        return Book.builder()
                .withTitle("Clean Code")
                .withAuthor("Robert Martin")
                .withIsbn(new Isbn13("9780135398579"))
                .withPrice(new Money(BigDecimal.valueOf(50L), Currency.EUR))
                .withPublicationYear(2008)
                .build();
    }

    public static BookDTO exampleBookDto() {
        return BookDTO.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("9780135398579")
                .price(Price.builder().value(50d).currency("EUR").build())
                .publicationYear(2008)
                .build();
    }
}
