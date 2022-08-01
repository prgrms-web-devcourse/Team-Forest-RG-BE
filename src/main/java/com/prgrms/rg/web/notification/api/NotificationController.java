package com.prgrms.rg.web.notification.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.notification.application.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping(value = "/api/v1/notification/eventstream", produces = "text/event-stream")
	public SseEmitter subscribe(
		@RequestHeader(value = "Authorization", defaultValue = "") String accessToken,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		log.info("connection request by " + accessToken);
		return notificationService.subscribe(accessToken, lastEventId);
	}
}
