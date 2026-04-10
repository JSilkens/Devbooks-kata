package be.jsilkens.devbooks.common.domain;

public interface DomainEventPublisher {
    void publish(DomainEvent domainEvent);
}
