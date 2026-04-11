package be.jsilkens.devbooks.shopping.db.repository;

import be.jsilkens.devbooks.shopping.db.entity.CartDb;
import be.jsilkens.devbooks.shopping.db.mapper.CartMapper;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.repository.BookRepository;
import be.jsilkens.devbooks.shopping.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Transactional
public class CartPersistenceFacade implements CartRepository {

    private static final UUID DEFAULT_CART_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private final CartJpaRepository cartJpaRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCart get() {
        return cartJpaRepository.findById(DEFAULT_CART_ID)
                .map(cartDb -> {
                    var isbns = cartDb.getItems().stream()
                            .map(item -> Isbn13.builder().withValue(item.getBookIsbn()).build())
                            .collect(Collectors.toSet());

                    var bookMap = bookRepository.findAllByIsbnIn(isbns).stream()
                            .collect(Collectors.toMap(Book::getIsbn, Function.identity()));

                    return CartMapper.map(cartDb, bookMap);
                })
                .orElseGet(ShoppingCart::empty);
    }

    @Override
    public void save(ShoppingCart cart) {
        var cartDb = cartJpaRepository.findById(DEFAULT_CART_ID)
                .orElseGet(() -> CartDb.builder().id(DEFAULT_CART_ID).build());

        var itemsDb = CartMapper.mapItems(cart, cartDb);

        cartDb.getItems().clear();
        cartDb.getItems().addAll(itemsDb);
        cartJpaRepository.save(cartDb);
    }
}
