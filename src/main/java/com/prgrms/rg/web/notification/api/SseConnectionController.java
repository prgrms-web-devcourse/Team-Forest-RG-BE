package com.prgrms.rg.web.notification.api;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.notification.model.sse.SseConnector;
import com.prgrms.rg.domain.notification.model.sse.SseNotificationSender;
import com.prgrms.rg.domain.notification.model.sse.SseResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseConnectionController {
	private final SseConnector connector;
	private final SseNotificationSender sender;

	@GetMapping(value = "/api/v1/connection/sse", produces = "text/event-stream")
	public SseEmitter subscribe(@AuthenticationPrincipal JwtAuthentication auth,
		HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("X-Accel-Buffering", "no");
		return connector.connect(auth.userId);
	}

	@GetMapping("/api/v1/connection/{userId}/health")
	public ResponseEntity<String> healthcheck(@PathVariable Long userId) {
		log.info("Health check requested by user " + userId);
		sender.sendNotification(userId, SseResponse.createConnectSuccess(userId), "health-check");
		return ResponseEntity.ok("job success");
	}
}
