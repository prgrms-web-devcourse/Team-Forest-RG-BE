package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.NotificationRepository;
import com.prgrms.rg.domain.user.model.User;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long>, NotificationRepository {
	Page<Notification> findAllByUser(User user, Pageable pageable);

	void deleteAllByUser(User user);

}
