package be.jsilkens.devbooks.shopping.db.entity;

import be.jsilkens.devbooks.shopping.domain.money.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder(setterPrefix = "with")
@Table(name = "BOOK")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookDb {

    @Id
    @Column(name = "ID", nullable = false, unique = true, updatable = false)
    private UUID id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @Column(name = "PUBLICATION_YEAR", nullable = false)
    private Integer publicationYear;

    @Column(name = "PRICE_AMOUNT", precision = 19, scale = 2, nullable = false)
    private BigDecimal priceAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRICE_CURRENCY", nullable = false)
    private Currency priceCurrency;

    @Column(name = "ISBN", nullable = false, unique = true)
    private String isbn;
}
