package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.entity.BookDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<BookDb, String> {

    Optional<BookDb> findByIsbn(String isbn);
}
