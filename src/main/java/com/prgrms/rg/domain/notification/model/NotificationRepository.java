package com.prgrms.rg.domain.notification.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prgrms.rg.domain.user.model.User;

public interface NotificationRepository {
	Notification save(Notification notification);

	Page<Notification> findAllByUser(User user, Pageable pageable);

	void deleteAllByUser(User user);

	long count();
}
