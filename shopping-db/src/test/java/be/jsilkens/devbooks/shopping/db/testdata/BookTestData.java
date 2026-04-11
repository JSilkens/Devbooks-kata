package be.jsilkens.devbooks.shopping.db.testdata;

import be.jsilkens.devbooks.shopping.db.entity.BookDb;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static be.jsilkens.devbooks.shopping.domain.money.Currency.EUR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookTestData {

    public static Book exampleBook() {
        return Book.builder()
                .withTitle("Clean Code")
                .withAuthor("Robert Martin")
                .withPublicationYear(2008)
                .withPrice(Money.builder()
                        .withAmount(new BigDecimal("50.00"))
                        .withCurrency(EUR)
                        .build())
                .withIsbn(Isbn13.builder()
                        .withValue("9780135398579")
                        .build())
                .build();
    }

    public static BookDb exampleBookDb() {
        String isbn = "9780135398579";
        return BookDb.builder()
                .withId(UUID.nameUUIDFromBytes(isbn.getBytes()))
                .withTitle("Clean Code")
                .withAuthor("Robert Martin")
                .withPublicationYear(2008)
                .withPriceAmount(new BigDecimal("50.00"))
                .withPriceCurrency(EUR)
                .withIsbn(isbn)
                .build();
    }
}
