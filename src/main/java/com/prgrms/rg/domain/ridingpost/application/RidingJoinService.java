package com.prgrms.rg.domain.ridingpost.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.event.DomainEventPublisher;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinFailException;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostFinder;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RidingJoinService {
	private final RidingPostFinder postFinder;
	private final UserReadService userReadService;
	private final RidingParticipantRepository ridingParticipantRepository;
	private final DomainEventPublisher eventPublisher;

	@Transactional
	public void joinUserToRiding(Long userId, Long ridingPostId) {
		try {
			User participant = userReadService.getUserEntityById(userId);
			RidingPost post = postFinder.find(ridingPostId);
			checkDuplicateJoin(participant, post);
			post.addParticipant(participant);
		} catch (RidingPostNotFoundException e) {
			throw new RidingJoinFailException(e);
		}
		RidingJoinEvent event = new RidingJoinEvent(userId, ridingPostId); //addParticipant 내부에서 이벤트 발행하는것을 고려해볼것
		eventPublisher.publish(event);
	}

	private void checkDuplicateJoin(User participant, RidingPost post) {
		if (ridingParticipantRepository.existsByUserAndPost(participant, post))
			throw new RidingJoinFailException("user already join riding");
	}

}