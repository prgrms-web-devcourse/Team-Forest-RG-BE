package com.prgrms.rg.web.user.requests;

import com.prgrms.rg.domain.user.application.command.ParticipantEvaluateCommand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantEvaluateRequest {

	private Long memberId;
	private boolean recommended;
	private boolean noshow;

	public ParticipantEvaluateCommand toCommand() {
		return new ParticipantEvaluateCommand(memberId, recommended, noshow);
	}

}
