package be.jsilkens.devbooks.shopping.domain;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.common.domain.validation.Validatable;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@ToString
public class Book implements Validatable {
    private final String title;
    private final String author;
    private final Integer publicationYear;
    private final Money price;
    private final Isbn13 isbn;

    @Override
    public Outcome<Book> validate() {
        return Outcome.merge(this, isbn.validate());
    }
}
