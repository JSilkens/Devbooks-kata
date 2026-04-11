package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.entity.BookDb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookDb, UUID> {
    Page<BookListItemView> findBy(Pageable pageable);

    Optional<BookDb> findByIsbn(String isbn);

    List<BookDb> findAllByIsbnIn(Collection<String> isbns);
}
