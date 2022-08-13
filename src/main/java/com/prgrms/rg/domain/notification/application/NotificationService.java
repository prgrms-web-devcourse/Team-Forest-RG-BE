package com.prgrms.rg.domain.notification.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.user.model.User;

public interface NotificationService {
	Notification createRidingJoinNotification(Long hostId, Long ridingPostId);

	Notification createJoinCancelNotification(Long hostId, Long ridingPostId);

	Page<Notification> loadPagedNotificationByUser(User user, Pageable pageable);

	void deleteAllNotificationByUser(Long userId);
}
