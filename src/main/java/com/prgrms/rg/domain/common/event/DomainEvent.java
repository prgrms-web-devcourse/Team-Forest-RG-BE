package com.prgrms.rg.domain.common.event;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public abstract class DomainEvent implements Serializable {
	private static final long serialVersionUID = 8945128060450240352L;
	private final LocalDateTime occurDate;

	protected DomainEvent() {
		occurDate = LocalDateTime.now();
	}
}
