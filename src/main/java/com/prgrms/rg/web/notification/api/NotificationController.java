package com.prgrms.rg.web.notification.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.NotificationInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class NotificationController {
	private final NotificationService notificationService;

	@Secured("ROLE_USER")
	@GetMapping("/api/v1/notifications")
	public Page<NotificationInfo> getNotifications(
		@PageableDefault(sort = "createdAt") Pageable pageable,
		@AuthenticationPrincipal JwtAuthentication auth) {
		return notificationService.loadPagedNotificationByUser(auth.userId, pageable);
	}

	@Secured("ROLE_USER")
	@DeleteMapping("/api/v1/notifications")
	public ResponseEntity<String> deleteNotifications(
		@AuthenticationPrincipal JwtAuthentication auth) {
		notificationService.deleteAllNotificationByUser(auth.userId);
		return ResponseEntity.ok("notification deleted");
	}
}
