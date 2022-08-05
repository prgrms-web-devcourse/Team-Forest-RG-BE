package com.prgrms.rg.domain.notification.application;

import com.prgrms.rg.domain.notification.model.Notification;

public interface NotificationService {
	Notification createRidingJoinNotification(Long hostId, Long ridingPostId);
}
