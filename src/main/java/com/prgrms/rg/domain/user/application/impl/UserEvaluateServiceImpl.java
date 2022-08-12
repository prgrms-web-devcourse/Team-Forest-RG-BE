package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipant;
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.user.application.UserEvaluateService;
import com.prgrms.rg.domain.user.application.command.EvaluatedFromLeaderCommand;
import com.prgrms.rg.domain.user.application.command.EvaluatedFromMemberCommand;
import com.prgrms.rg.domain.user.model.EvaluationgManagement;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserEvaluateServiceImpl implements UserEvaluateService {

	private final RidingPostReadService postReadService;
	private final EvaluationgManagement evaluationgManagement;

	@Override
	public void evaluateFromLeader(Long userId, Long postId, List<EvaluatedFromLeaderCommand> commandList) {

		var ridingpost = postReadService.loadRidingPostById(postId);
		//user가 leader인지 확인
		checkArgument(ridingpost.getLeader().getId().equals(userId), new UnAuthorizedException(userId));

		var members = ridingpost.getRidingParticipantSection().getParticipants();
		var evaluator = findParticipant(members, userId);
		//평가를 진행하였는지 여부를 확인
		evaluator.checkEvaluated();

		for (EvaluatedFromLeaderCommand command : commandList) {
			evaluationgManagement.evaluate(members, evaluator, command.getMemberId(), command.isRecommended(), command.isNoshow());
		}
		evaluator.setEvaluated();
	}

	public void evaluateFromMember(Long userId, Long postId, List<EvaluatedFromMemberCommand> commandList) {
		var ridingpost = postReadService.loadRidingPostById(postId);
		var members = ridingpost.getRidingParticipantSection().getParticipants();
		var evaluator = findParticipant(members, userId);
		//평가를 진행하였는지 여부를 확인
		evaluator.checkEvaluated();
		for (EvaluatedFromMemberCommand command : commandList) {
			evaluationgManagement.evaluate(members, evaluator, command.getMemberId(), command.isRecommended());
		}
		evaluator.setEvaluated();
	}

	private RidingParticipant findParticipant(List<RidingParticipant> participantList, Long participantId) {
		for (RidingParticipant participant : participantList) {
			if (participant.getUser().getId().equals(participantId))
				return participant;
		}
		throw new UnAuthorizedException(participantId);
	}
}
