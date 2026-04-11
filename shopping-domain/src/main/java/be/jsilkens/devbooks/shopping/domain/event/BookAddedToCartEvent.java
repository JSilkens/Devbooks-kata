package be.jsilkens.devbooks.shopping.domain.event;

import be.jsilkens.devbooks.common.domain.event.DomainEvent;
import be.jsilkens.devbooks.common.domain.event.DomainEventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class BookAddedToCartEvent implements DomainEvent {

    private final String isbn;

    @Override
    public DomainEventType getType() {
        return () -> "BOOK_ADDED_TO_CART";
    }

    @Override
    public Object getPayload() {
        return this;
    }
}
