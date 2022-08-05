package com.prgrms.rg.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.prgrms.rg.domain.common.event.DomainEvent;
import com.prgrms.rg.domain.common.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class SpringEventPublisher implements DomainEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public <T extends DomainEvent> void publish(T event) {
		applicationEventPublisher.publishEvent(event);
	}

}
