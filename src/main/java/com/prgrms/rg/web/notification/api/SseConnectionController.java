package com.prgrms.rg.web.notification.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.notification.model.sse.SseConnector;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class SseConnectionController {
	private final SseConnector connector;

	@Secured("ROLE_USER")
	@GetMapping(value = "/api/v1/connection/sse/{userId}", produces = "text/event-stream")
	public SseEmitter subscribe(@AuthenticationPrincipal JwtAuthentication auth) {
		return connector.connect(auth.userId);
	}
}
