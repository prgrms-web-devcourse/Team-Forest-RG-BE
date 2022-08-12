package com.prgrms.rg.web.user.requests;

import com.prgrms.rg.domain.user.application.command.EvaluatedFromLeaderCommand;

import lombok.Data;

@Data
public class EvaluatedFromLeaderRequest {

	private final Long memberId;
	private final boolean recommended;
	private final boolean noshow;

	public EvaluatedFromLeaderCommand toCommand() {
		return new EvaluatedFromLeaderCommand(memberId, recommended, noshow);
	}

}
