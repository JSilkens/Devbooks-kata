package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class GetBookByISBNUseCase {

    private final BookRepository bookRepository;

    public Outcome<Optional<Book>> execute(String isbnValue) {

        return Isbn13.builder()
                .withValue(isbnValue)
                .build()
                .validate()
                .map(bookRepository::findByIsbn);
    }
}
