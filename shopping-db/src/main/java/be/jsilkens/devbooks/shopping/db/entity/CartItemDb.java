package be.jsilkens.devbooks.shopping.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDb {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private CartDb cart;

    @Column(name = "book_isbn", nullable = false)
    private String bookIsbn;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
