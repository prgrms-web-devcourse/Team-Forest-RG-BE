package com.prgrms.rg.web.user.requests;

import com.prgrms.rg.domain.user.application.command.EvaluatedFromMemberCommand;

import lombok.Data;

@Data
public class EvaluatedFromMemberRequest {
	private final Long memberId;
	private final boolean recommended;

	public EvaluatedFromMemberCommand toCommand() {
		return new EvaluatedFromMemberCommand(memberId, recommended);
	}
}
