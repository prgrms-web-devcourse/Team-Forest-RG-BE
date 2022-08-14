package com.prgrms.rg.domain.notification.application;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.NotificationInfo;

public interface NotificationService {
	Notification createRidingJoinNotification(Long hostId, Long ridingPostId);

	Notification createJoinCancelNotification(Long hostId, Long ridingPostId);

	List<Notification> createRidingDeleteNotification(Long ridingPostId);

	Page<NotificationInfo> loadPagedNotificationByUser(Long userId, Pageable pageable);

	void deleteAllNotificationByUser(Long userId);
}
