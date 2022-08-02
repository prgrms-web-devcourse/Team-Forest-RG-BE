package com.prgrms.rg.domain.common.event;

public interface DomainEventPublisher {
	<T extends DomainEvent> void publish(T event);
}
