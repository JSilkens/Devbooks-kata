package be.jsilkens.devbooks.shopping.usecase;

import be.jsilkens.devbooks.common.domain.PaginatedResult;
import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.BookListItem;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class GetPaginatedBooksUseCase {

    private final BookRepository bookRepository;

    public Outcome<PaginatedResult<BookListItem>> execute(int page, int size) {
        if (page < 1) {
            return new Outcome.Failure<>("Page index must be at least 1");
        }
        if (size < 1) {
            return new Outcome.Failure<>("Page size must be at least 1");
        }

        return new Outcome.Success<>(bookRepository.findAll(page, size));
    }
}
