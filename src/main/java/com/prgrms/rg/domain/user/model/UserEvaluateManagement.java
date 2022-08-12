package com.prgrms.rg.domain.user.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.prgrms.rg.domain.ridingpost.model.RidingParticipant;
import com.prgrms.rg.domain.user.application.exception.EvaluationFailException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEvaluateManagement {

	public void evaluate(List<RidingParticipant> participantList, RidingParticipant evaluator, Long memberId,
		boolean recommended, boolean noshow) {
		if (evaluator.getUser().getId().equals(memberId))
			throw new EvaluationFailException();
		var member = checkAndFindParticipant(participantList, memberId).getUser();

		if (recommended)
			member.getManner().addPoint();
		if (noshow)
			member.getManner().addNoShowCount();

	}

	public void evaluate(List<RidingParticipant> participantList, RidingParticipant evaluator, Long memberId,
		boolean recommended) {
		if (evaluator.getUser().getId().equals(memberId))
			throw new EvaluationFailException();
		var member  = checkAndFindParticipant(participantList, memberId).getUser();

		if (recommended)
			member.getManner().addPoint();
	}

	private RidingParticipant checkAndFindParticipant(List<RidingParticipant> participantList, Long participantId) {
		for (RidingParticipant participant : participantList) {
			if (participant.getUser().getId().equals(participantId))
				return participant;
		}
		throw new EvaluationFailException();
	}
}
