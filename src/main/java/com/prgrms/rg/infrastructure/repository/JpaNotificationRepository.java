package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.notification.model.Notification;
import com.prgrms.rg.domain.notification.model.NotificationRepository;

public interface JpaNotificationRepository extends JpaRepository<Notification, Long>, NotificationRepository {
}
