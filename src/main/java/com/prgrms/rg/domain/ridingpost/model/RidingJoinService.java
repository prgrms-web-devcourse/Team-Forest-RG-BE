package com.prgrms.rg.domain.ridingpost.model;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.event.DomainEventPublisher;
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

	// 1. User가 참여 요청시 처리해야 할 일들은
	// - 이미 참여한 사람은 아닌지 검증
	// - 라이딩 게시글의 참가자에 포함 시키고(Participant 엔티티 추가 transaction)
	// - 라이딩 리더에게 참가자가 늘었다는 알림을 보내기(이벤트로 처리)

	@Transactional
	public void joinUserToRiding(Long userId, Long ridingPostId) {
		User user = userReadService.getUserEntityById(userId);
		RidingPost post = postFinder.find(ridingPostId);
		if (ridingParticipantRepository.existsByUserAndPost(user, post))
			throw new RidingJoinFailException("user already join riding");
		post.addParticipant(user);
		RidingJoinEvent event = new RidingJoinEvent(userId, ridingPostId); //addParticipant 내부에서 이벤트 발행하는것을 고려해볼것
		eventPublisher.publish(event);
	}

}