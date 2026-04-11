CREATE TABLE shopping_cart
(
    id UUID NOT NULL,
    CONSTRAINT pk_shopping_cart PRIMARY KEY (id)
);

CREATE TABLE cart_item
(
    id               UUID         NOT NULL,
    shopping_cart_id UUID         NOT NULL,
    book_isbn        VARCHAR(255) NOT NULL,
    quantity         INTEGER      NOT NULL,
    CONSTRAINT pk_cart_item PRIMARY KEY (id),
    CONSTRAINT fk_cart_item_on_shopping_cart FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (id),
    CONSTRAINT fk_cart_item_on_book FOREIGN KEY (book_isbn) REFERENCES book (isbn)
);

-- Seed a default cart for the singleton approach
INSERT INTO shopping_cart (id)
VALUES ('00000000-0000-0000-0000-000000000001');
