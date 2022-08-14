package com.prgrms.rg.domain.ridingpost.model.event;

import com.prgrms.rg.domain.common.event.DomainEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RidingPostDeleteEvent extends DomainEvent {
	private final Long postId;
}
