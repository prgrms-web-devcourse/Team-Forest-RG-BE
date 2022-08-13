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
import com.prgrms.rg.domain.user.application.command.ParticipantEvaluateCommand;
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
	public void evaluateMembers(Long userId, Long postId, List<ParticipantEvaluateCommand> commandList) {

		var ridingpost = postReadService.loadRidingPostById(postId);

		var members = ridingpost.getRidingParticipantSection().getParticipants();
		var evaluator = findParticipant(members, userId);
		//평가를 진행하였는지 여부를 확인
		checkEnabledEvaluation(ridingpost, evaluator);

		//leader evaluate
		if (ridingpost.getLeader().getId().equals(userId)) {
			for (ParticipantEvaluateCommand command : commandList) {
				userEvaluateManagement.evaluateFromLeader(members, evaluator, command.getMemberId(), command.isRecommended(),
					command.isNoshow());
			}
		} else { //member evaluate
			for (ParticipantEvaluateCommand command : commandList) {
				userEvaluateManagement.evaluateFromMember(members, evaluator, command.getMemberId(), command.isRecommended());
			}
		}

		evaluator.setEvaluated();
	}

	/**
	 * 평가 진행 여부, 진행 가능 여부(riding 시작 시점 이후인지, 이후 7일 이상 경과하였는지) 확인
	 */
	private void checkEnabledEvaluation(RidingPost riding, RidingParticipant participant) {
		var ridingDate = riding.getRidingMainSection().getRidingDate();
		checkArgument(LocalDateTime.now().isAfter(ridingDate) &&
				LocalDateTime.now().isBefore(ridingDate.plusDays(7L)),
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
