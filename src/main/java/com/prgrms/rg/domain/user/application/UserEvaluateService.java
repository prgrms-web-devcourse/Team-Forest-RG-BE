package com.prgrms.rg.domain.user.application;

import java.util.List;

import com.prgrms.rg.domain.user.application.command.EvaluatedFromLeaderCommand;
import com.prgrms.rg.domain.user.application.command.EvaluatedFromMemberCommand;

public interface UserEvaluateService {
	void evaluateFromLeader(Long userId, Long postId, List<EvaluatedFromLeaderCommand> commandList);

	void evaluateFromMember(Long userId, Long postId, List<EvaluatedFromMemberCommand> commandList);
}
