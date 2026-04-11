CREATE TABLE book
(
    id               UUID           NOT NULL,
    title            VARCHAR(255)   NOT NULL,
    author           VARCHAR(255)   NOT NULL,
    publication_year INTEGER        NOT NULL,
    price_amount     NUMERIC(19, 2) NOT NULL,
    price_currency   VARCHAR(255)   NOT NULL,
    isbn             VARCHAR(255)   NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id),
    CONSTRAINT uk_book_isbn UNIQUE (isbn)
);

CREATE INDEX idx_book_title ON book (title);
