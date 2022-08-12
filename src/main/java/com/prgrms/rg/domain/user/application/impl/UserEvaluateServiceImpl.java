package com.prgrms.rg.domain.user.application.impl;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipant;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.user.application.UserEvaluateService;
import com.prgrms.rg.domain.user.application.command.EvaluatedFromLeaderCommand;
import com.prgrms.rg.domain.user.application.command.EvaluatedFromMemberCommand;
import com.prgrms.rg.domain.user.application.exception.EvaluationFailException;
import com.prgrms.rg.domain.user.model.UserEvaluateManagement;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserEvaluateServiceImpl implements UserEvaluateService {

	private final RidingPostReadService postReadService;
	private final UserEvaluateManagement userEvaluateManagement;

	@Override
	public void evaluateFromLeader(Long userId, Long postId, List<EvaluatedFromLeaderCommand> commandList) {

		var ridingpost = postReadService.loadRidingPostById(postId);

		//user가 leader인지 확인
		checkArgument(ridingpost.getLeader().getId().equals(userId), new UnAuthorizedException(userId));

		var members = ridingpost.getRidingParticipantSection().getParticipants();
		var evaluator = findParticipant(members, userId);
		//평가를 진행하였는지 여부를 확인

		checkEnabledEvaluation(ridingpost, evaluator);
		for (EvaluatedFromLeaderCommand command : commandList) {
			userEvaluateManagement.evaluate(members, evaluator, command.getMemberId(), command.isRecommended(), command.isNoshow());
		}
		evaluator.setEvaluated();
	}

	@Override
	public void evaluateFromMember(Long userId, Long postId, List<EvaluatedFromMemberCommand> commandList) {
		var ridingpost = postReadService.loadRidingPostById(postId);
		var members = ridingpost.getRidingParticipantSection().getParticipants();
		var evaluator = findParticipant(members, userId);

		checkEnabledEvaluation(ridingpost, evaluator);
		for (EvaluatedFromMemberCommand command : commandList) {
			userEvaluateManagement.evaluate(members, evaluator, command.getMemberId(), command.isRecommended());
		}
		evaluator.setEvaluated();
	}

	/**
	 * 평가 진행 여부, 진행 가능 여부(riding 시작 시점 이후인지) 확인
	 */
	private void checkEnabledEvaluation(RidingPost riding, RidingParticipant participant) {
		checkArgument(riding.getRidingMainSection().getRidingDate().isBefore(LocalDateTime.now()),
			new EvaluationFailException());
		checkArgument(!participant.isEvaluated(), new EvaluationFailException());

	}

	/**
	 * 해당 리스트에서 participant 조회
	 * 존재하지 않을 시 -> 허가되지 않은 접근 예외
	 */
	private RidingParticipant findParticipant(List<RidingParticipant> participantList, Long participantId) {
		for (RidingParticipant participant : participantList) {
			if (participant.getUser().getId().equals(participantId))
				return participant;
		}
		throw new UnAuthorizedException(participantId);
	}
}
