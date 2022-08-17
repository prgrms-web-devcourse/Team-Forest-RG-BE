package com.prgrms.rg.domain.notification.application.impl;

import static org.springframework.transaction.event.TransactionPhase.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.sse.SseNotificationSender;
import com.prgrms.rg.domain.notification.model.sse.SseResponse;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinCancelEvent;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;
import com.prgrms.rg.domain.ridingpost.model.event.RidingPostDeleteEvent;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationPushService {
	private static final String NOTIFICATION_OCCUR_EVENT = "notification_occur";
	private final NotificationService notificationService;
	private final SseNotificationSender notificationSender;
	private final RidingPostReadService postReadService;

	@TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleRidingJoinEvent(RidingJoinEvent event) {
		RidingPost post = postReadService.loadRidingPostById(event.getPostId());
		User host = post.getLeader();
		Notification notification = notificationService.createRidingJoinNotification(host.getId(), post.getId());
		notificationSender.sendNotification(host.getId(), new SseResponse(notification.getType().toString()),
			NOTIFICATION_OCCUR_EVENT);
	}

	@TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleRidingJoinCancelEvent(RidingJoinCancelEvent event) {
		RidingPost post = postReadService.loadRidingPostById(event.getPostId());
		User host = post.getLeader();
		Notification notification = notificationService.createJoinCancelNotification(host.getId(), post.getId());
		notificationSender.sendNotification(host.getId(), notification, NOTIFICATION_OCCUR_EVENT);
	}

	@TransactionalEventListener(phase = AFTER_COMMIT, fallbackExecution = true)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleRidingDeleteEvent(RidingPostDeleteEvent event) {
		RidingPost post = postReadService.loadRidingPostById(event.getPostId());
		List<Notification> notifications = notificationService.createRidingDeleteNotification(post.getId());
		notifications.forEach(
			notification -> notificationSender.sendNotification(notification.getUser().getId(), notification,
				NOTIFICATION_OCCUR_EVENT));
	}
}
