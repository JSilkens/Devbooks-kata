package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.entity.BookDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookDb, UUID> {

    Optional<BookDb> findByIsbn(String isbn);
}
