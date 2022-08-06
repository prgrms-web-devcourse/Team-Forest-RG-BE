package com.prgrms.rg.domain.notification.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.NotificationRepository;
import com.prgrms.rg.domain.ridingpost.application.impl.RidingPostReadServiceImpl;
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
	private final RidingPostReadServiceImpl postReadService;

	@Override
	@Transactional
	public Notification createRidingJoinNotification(Long userId, Long ridingPostId) {
		User user = userReadService.getUserEntityById(userId);
		RidingPost post = postReadService.getRidingPostEntityById(ridingPostId);
		Notification notification = Notification.createRidingJoinNotification(user, post);
		return notificationRepository.save(notification);
	}
}
