package be.jsilkens.devbooks.common.domain.event;

public interface DomainEventPublisher {
    void publish(DomainEvent domainEvent);
}
