package com.prgrms.rg.domain.ridingpost.model.event;

import com.prgrms.rg.domain.common.event.DomainEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RidingJoinEvent extends DomainEvent {
	private final Long participantId;
	private final Long postId;
}
