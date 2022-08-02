package com.prgrms.rg.domain.notification.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.NotificationRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingPostFinder;
import com.prgrms.rg.domain.ridingpost.model.dummy.RidingPost;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserFinder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserFinder userFinder;
	private final RidingPostFinder postFinder;

	@Override
	@Transactional
	public Notification createRidingJoinNotification(Long userId, Long ridingPostId) {
		User user = userFinder.find(userId);
		RidingPost post = postFinder.find(ridingPostId);
		Notification notification = Notification.createRidingJoinNotification(user, post);
		return notificationRepository.save(notification);
	}
}
