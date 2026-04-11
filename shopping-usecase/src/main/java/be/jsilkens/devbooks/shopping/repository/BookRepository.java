package be.jsilkens.devbooks.shopping.repository;

import be.jsilkens.devbooks.common.domain.PaginatedResult;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.BookListItem;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;

import java.util.Optional;

public interface BookRepository {

    Optional<Book> findByIsbn(Isbn13 isbn);

    PaginatedResult<BookListItem> findAll(int page, int size);
}
