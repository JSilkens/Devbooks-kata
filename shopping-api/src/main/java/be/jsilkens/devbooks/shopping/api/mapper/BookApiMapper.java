package be.jsilkens.devbooks.shopping.api.mapper;

import be.jsilkens.devbooks.shopping.api.model.BookDTO;
import be.jsilkens.devbooks.shopping.api.model.Price;
import be.jsilkens.devbooks.shopping.domain.Book;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookApiMapper {

    public static BookDTO map(Book book) {
        var price = new Price();
        price.setValue(book.getPrice().amount().doubleValue());
        price.setCurrency(book.getPrice().currency().name());

        var bookDTO = new BookDTO();
        bookDTO.setIsbn(book.getIsbn().value());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setPrice(price);

        return bookDTO;
    }
}
