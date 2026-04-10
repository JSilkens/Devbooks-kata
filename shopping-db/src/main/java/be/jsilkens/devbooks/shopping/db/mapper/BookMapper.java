package be.jsilkens.devbooks.shopping.db.mapper;

import be.jsilkens.devbooks.shopping.db.entity.BookDb;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookMapper {

    public static BookDb map(Book book) {
        return BookDb.builder()
                .withId(UUID.nameUUIDFromBytes(book.getIsbn().value().getBytes()))
                .withTitle(book.getTitle())
                .withAuthor(book.getAuthor())
                .withPublicationYear(book.getPublicationYear())
                .withPriceAmount(book.getPrice().amount())
                .withPriceCurrency(book.getPrice().currency())
                .withIsbn(book.getIsbn().value())
                .build();
    }

    public static Book map(BookDb bookDb) {
        return Book.builder()
                .withTitle(bookDb.getTitle())
                .withAuthor(bookDb.getAuthor())
                .withPublicationYear(bookDb.getPublicationYear())
                .withPrice(new Money(bookDb.getPriceAmount(), bookDb.getPriceCurrency()))
                .withIsbn(new Isbn13(bookDb.getIsbn()))
                .build();
    }
}
