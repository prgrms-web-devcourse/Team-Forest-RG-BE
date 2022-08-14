package com.prgrms.rg.web.notification.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping("/api/notifications")
	public Page<Notification> getNotifications(
		@PageableDefault(sort = "createdAt") Pageable pageable,
		@AuthenticationPrincipal JwtAuthentication auth) {
		return notificationService.loadPagedNotificationByUser(auth.userId, pageable);
	}

}
