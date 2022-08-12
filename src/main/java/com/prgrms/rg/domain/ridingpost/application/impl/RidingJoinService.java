package com.prgrms.rg.domain.ridingpost.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.event.DomainEventPublisher;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinCancelEvent;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;
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

	public void joinUserToRiding(Long userId, Long ridingPostId) {
		try {
			User participant = userReadService.getUserEntityById(userId);
			RidingPost post = postFinder.loadRidingPostById(ridingPostId);
			checkDuplicateJoin(participant, post);
			post.addParticipant(participant);
		} catch (RidingPostNotFoundException e) {
			throw new RidingJoinFailException(e);
		}
		RidingJoinEvent event = new RidingJoinEvent(userId, ridingPostId);
		eventPublisher.publish(event);
	}

	public void cancelJoin(Long userId, Long ridingPostId) {
		try {
			User participant = userReadService.getUserEntityById(userId);
			RidingPost post = postFinder.loadRidingPostById(ridingPostId);
			checkIsRidingMember(participant, post);
			post.removeParticipant(participant);
		} catch (RidingPostNotFoundException e) {
			throw new RidingJoinCancelFailException(e.getMessage());
		}
		RidingJoinCancelEvent event = new RidingJoinCancelEvent(userId, ridingPostId);
		eventPublisher.publish(event);
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