package com.prgrms.rg.domain.notification.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.NotificationRepository;
import com.prgrms.rg.domain.notification.model.NotificationType;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserReadService userReadService;
	private final RidingPostReadService postReadService;

	@Override
	@Transactional
	public Notification createRidingJoinNotification(Long userId, Long ridingPostId) {
		User user = userReadService.getUserEntityById(userId);
		RidingPost post = postReadService.loadRidingPostById(ridingPostId);
		Notification notification = Notification.createNotification(user, post, NotificationType.RIDING_JOIN);
		return notificationRepository.save(notification);
	}

	@Override
	public Notification createJoinCancelNotification(Long hostId, Long ridingPostId) {
		User user = userReadService.getUserEntityById(hostId);
		RidingPost post = postReadService.loadRidingPostById(ridingPostId);
		Notification notification = Notification.createNotification(user, post, NotificationType.RIDING_JOIN_CANCEL);
		return notificationRepository.save(notification);
	}

	@Override
	public Page<Notification> loadPagedNotificationByUser(User user, Pageable pageable) {
		return notificationRepository.findAllByUser(user, pageable);
	}

	public void deleteAllNotificationByUser(Long userId) {
		User user = userReadService.getUserEntityById(userId);
		notificationRepository.deleteAllByUser(user);
	}
}
