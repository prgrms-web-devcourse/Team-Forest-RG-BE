package com.prgrms.rg.domain.user.application;

import java.util.List;

import com.prgrms.rg.domain.user.application.command.ParticipantEvaluateCommand;

public interface UserEvaluateService {
	void evaluateMembers(Long userId, Long postId, List<ParticipantEvaluateCommand> commandList);
}
