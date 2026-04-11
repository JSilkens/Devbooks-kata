package be.jsilkens.devbooks.common.domain.event;

public interface DomainEvent {

    DomainEventType getType();

    Object getPayload();
}
