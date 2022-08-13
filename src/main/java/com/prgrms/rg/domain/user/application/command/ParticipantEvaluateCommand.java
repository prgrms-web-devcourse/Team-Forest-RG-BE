package com.prgrms.rg.domain.user.application.command;

import lombok.Data;

@Data
public class ParticipantEvaluateCommand {

	private final Long memberId;
	private final boolean recommended;
	private final boolean noshow;

}
