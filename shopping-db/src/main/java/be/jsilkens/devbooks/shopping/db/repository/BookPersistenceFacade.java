package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.mapper.BookMapper;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookPersistenceFacade implements BookRepository {

    private final BookJpaRepository bookJpaRepository;

    @Override
    public Optional<Book> findByIsbn(Isbn13 isbn) {
        return bookJpaRepository.findByIsbn(isbn.value()).map(BookMapper::map);

    }
}
