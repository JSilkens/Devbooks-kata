package be.jsilkens.devbooks.shopping.api.controller;

import be.jsilkens.devbooks.common.domain.PaginatedResult;
import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.api.mapper.BookApiMapper;
import be.jsilkens.devbooks.shopping.api.model.BookDTO;
import be.jsilkens.devbooks.shopping.api.model.PaginatedBookResponseDTO;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.BookListItem;
import be.jsilkens.devbooks.shopping.usecase.GetBookByISBNUseCase;
import be.jsilkens.devbooks.shopping.usecase.GetPaginatedBooksUseCase;
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
    private final GetPaginatedBooksUseCase getPaginatedBooksUseCase;

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

    @Override
    public ResponseEntity<PaginatedBookResponseDTO> getPaginatedBooks(Integer page, Integer size) {
        var pageOptional = Optional.ofNullable(page).orElse(1);
        var sizeOptional = Optional.ofNullable(size).orElse(10);

        var result = getPaginatedBooksUseCase.execute(pageOptional, sizeOptional);

        if (result instanceof Outcome.Failure) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ((Outcome.Failure<PaginatedResult<BookListItem>>) result).getMessage());
        }

        return ResponseEntity.ok(BookApiMapper.map(((Outcome.Success<PaginatedResult<BookListItem>>) result).getValue()));
    }
}
