package be.jsilkens.devbooks.shopping.api.mapper;

import be.jsilkens.devbooks.common.domain.PaginatedResult;
import be.jsilkens.devbooks.common.domain.PaginationMetadata;
import be.jsilkens.devbooks.shopping.api.model.*;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.BookListItem;
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

    public static BookListItemDTO map(BookListItem item) {
        var price = new Price();
        price.setValue(item.price().amount().doubleValue());
        price.setCurrency(item.price().currency().name());

        var bookListItemDTO = new BookListItemDTO();
        bookListItemDTO.setTitle(item.title());
        bookListItemDTO.setAuthor(item.author());
        bookListItemDTO.setPrice(price);
        bookListItemDTO.setDetailsLink("/api/book/" + item.isbn().value());

        return bookListItemDTO;
    }

    public static PaginatedBookResponseDTO map(PaginatedResult<BookListItem> result) {
        var responseDTO = new PaginatedBookResponseDTO();
        responseDTO.setItems(result.items().stream()
                .map(BookApiMapper::map)
                .toList());
        responseDTO.setMetadata(map(result.metadata()));
        return responseDTO;
    }

    private static PaginationMetadataDTO map(PaginationMetadata metadata) {
        var metadataDTO = new PaginationMetadataDTO();
        metadataDTO.setCurrentPage(metadata.currentPage());
        metadataDTO.setPageSize(metadata.pageSize());
        metadataDTO.setTotalItems(metadata.totalItems());
        metadataDTO.setTotalPages(metadata.totalPages());
        return metadataDTO;
    }
}
