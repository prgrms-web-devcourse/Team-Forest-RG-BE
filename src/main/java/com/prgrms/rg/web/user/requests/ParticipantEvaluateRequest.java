package com.prgrms.rg.web.user.requests;

import com.prgrms.rg.domain.user.application.command.ParticipantEvaluateCommand;

import lombok.Data;

@Data
public class ParticipantEvaluateRequest {

	private final Long memberId;
	private final boolean recommended;
	private final boolean noshow;

	public ParticipantEvaluateCommand toCommand() {
		return new ParticipantEvaluateCommand(memberId, recommended, noshow);
	}

}
