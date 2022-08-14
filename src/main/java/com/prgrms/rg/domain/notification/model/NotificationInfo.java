package com.prgrms.rg.domain.notification.model;

import lombok.Data;

@Data
public class NotificationInfo {
	private Long id;
	private Long ridingId;
	private NotificationType type;
	private boolean isRead;
	private String contents;

	public static NotificationInfo from(Notification notification) {
		NotificationInfo instance = new NotificationInfo();
		instance.setId(notification.getId());
		instance.setRidingId(notification.getRidingPost().getId());
		instance.setType(notification.getType());
		instance.setContents(notification.getContents());
		instance.setRead(notification.isRead());
		return instance;
	}
}
