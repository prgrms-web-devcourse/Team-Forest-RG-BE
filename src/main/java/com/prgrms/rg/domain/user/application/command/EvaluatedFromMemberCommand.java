package com.prgrms.rg.domain.user.application.command;

import lombok.Data;

@Data
public class EvaluatedFromMemberCommand {
	private final Long memberId;
	private final boolean recommended;
}
