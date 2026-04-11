package be.jsilkens.devbooks.application.config;

import be.jsilkens.devbooks.common.domain.event.DomainEvent;
import be.jsilkens.devbooks.common.domain.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent domainEvent) {
        log.info("Publishing domain event of type {} with payload {}", domainEvent.getType().name(), domainEvent.getPayload());
        applicationEventPublisher.publishEvent(domainEvent);
    }
}
