package be.jsilkens.devbooks.shopping.repository;

import be.jsilkens.devbooks.shopping.domain.ShoppingCart;

public interface CartRepository {
    ShoppingCart get();

    void save(ShoppingCart cart);
}
