package com.prgrms.rg.domain.ridingpost.application.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.event.DomainEventPublisher;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinCancelEvent;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;
import com.prgrms.rg.domain.ridingpost.model.exception.CancelDeadlineOverException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinCancelFailException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinFailException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RidingJoinService {
	private final RidingPostReadService postFinder;
	private final UserReadService userReadService;
	private final RidingParticipantRepository ridingParticipantRepository;
	private final DomainEventPublisher eventPublisher;

	public void joinUserToRiding(Long participantId, Long ridingPostId) {
		try {
			User participant = userReadService.getUserEntityById(participantId);
			RidingPost post = postFinder.loadRidingPostById(ridingPostId);
			checkDuplicateJoin(participant, post);
			post.join(participant);
		} catch (RidingPostNotFoundException | IllegalArgumentException e) {
			throw new RidingJoinFailException(e);
		}
		RidingJoinEvent event = new RidingJoinEvent(participantId, ridingPostId);
		eventPublisher.publish(event);
	}

	public void cancelJoin(Long userId, Long ridingPostId) {
		try {
			User participant = userReadService.getUserEntityById(userId);
			RidingPost post = postFinder.loadRidingPostById(ridingPostId);
			checkCancelable(participant, post);
			post.removeParticipant(participant);
		} catch (RidingPostNotFoundException e) {
			throw new RidingJoinCancelFailException(e.getMessage());
		}
		RidingJoinCancelEvent event = new RidingJoinCancelEvent(userId, ridingPostId);
		eventPublisher.publish(event);
	}

	private void checkCancelable(User participant, RidingPost post) {
		checkIsRidingMember(participant, post);
		checkCancelDeadline(post);
	}

	private void checkCancelDeadline(RidingPost post) {
		LocalDateTime ridingDate = post.getRidingMainSection().getRidingDate();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime cancelableTime = ridingDate.minusHours(2);
		if (now.isAfter(cancelableTime))
			throw new CancelDeadlineOverException("cancel is allowed only 2 hours before riding");
	}

	private void checkDuplicateJoin(User participant, RidingPost post) {
		if (ridingParticipantRepository.existsByUserAndPost(participant, post))
			throw new RidingJoinFailException("user already join riding");
	}

	private void checkIsRidingMember(User participant, RidingPost post) {
		if (!ridingParticipantRepository.existsByUserAndPost(participant, post))
			throw new RidingJoinCancelFailException("this user not participate in riding '" + post.getId() + "'");
	}
}