package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.common.domain.PaginatedResult;
import be.jsilkens.devbooks.common.domain.PaginationMetadata;
import be.jsilkens.devbooks.shopping.db.mapper.BookMapper;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.BookListItem;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public PaginatedResult<BookListItem> findAll(int page, int size) {
        var pageable = PageRequest.of(page - 1, size);
        var pagedResult = bookJpaRepository.findAllProjectedBy(pageable);

        var items = pagedResult.getContent().stream()
                .map(BookMapper::map)
                .toList();

        var metadata = new PaginationMetadata(
                page,
                size,
                pagedResult.getTotalElements(),
                pagedResult.getTotalPages()
        );

        return new PaginatedResult<>(items, metadata);
    }
}
