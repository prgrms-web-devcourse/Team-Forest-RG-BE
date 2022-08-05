package com.prgrms.rg.web.notification.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.notification.model.sse.SseConnector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseConnectionController {
	private final SseConnector connector;

	@GetMapping(value = "/api/v1/connection/sse", produces = "text/event-stream")
	public SseEmitter subscribe(
		@RequestHeader(value = "Authorization", defaultValue = "") String accessToken,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		log.info("connection request by " + accessToken);
		return connector.connect(1L);
	}
}
