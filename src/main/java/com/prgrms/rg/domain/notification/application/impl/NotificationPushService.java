package com.prgrms.rg.domain.notification.application.impl;

import static org.springframework.transaction.event.TransactionPhase.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.sse.SseNotificationSender;
import com.prgrms.rg.domain.ridingpost.model.RidingPostFinder;
import com.prgrms.rg.domain.ridingpost.model.dummy.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPushService {
	private static final String RIDING_JOIN_EVENT = "riding_join_event";
	private final NotificationService notificationService;
	private final SseNotificationSender notificationSender;
	private final RidingPostFinder postFinder;

	// 알림 엔티티를 생성해 db에 저장하고
	// 사용자의 emitter를 찾아서
	// 알림을 보냅니다
	@TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleRidingJoinEvent(RidingJoinEvent event) {
		RidingPost post = postFinder.find(event.getPostId());
		User host = post.getHost();
		Notification notification = notificationService.createRidingJoinNotification(host.getId(), post.getId());
		notificationSender.sendNotification(host.getId(), notification, RIDING_JOIN_EVENT);
	}

}
