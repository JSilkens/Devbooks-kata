package be.jsilkens.devbooks.common.domain;

public interface DomainEvent {

    DomainEventType getType();

    Object getPayload();
}
