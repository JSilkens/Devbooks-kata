package be.jsilkens.devbooks.shopping.api.controller;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.api.mapper.BookApiMapper;
import be.jsilkens.devbooks.shopping.api.model.BookDTO;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.usecase.GetBookByISBNUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {

    private final GetBookByISBNUseCase getBookByISBNUseCase;

    @Override
    public ResponseEntity<BookDTO> getBookByIsbn(String isbn) {
        var retrievedBookResult = getBookByISBNUseCase.execute(isbn);

        if (retrievedBookResult instanceof Outcome.Failure) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ((Outcome.Failure<Optional<Book>>) retrievedBookResult).getMessage());
        }

        return ((Outcome.Success<Optional<Book>>) retrievedBookResult).getValue()
                .map(BookApiMapper::map)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }
}
