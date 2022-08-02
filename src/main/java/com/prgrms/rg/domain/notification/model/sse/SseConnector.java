package com.prgrms.rg.domain.notification.model.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SseConnector {
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
	private final SseEmitterRepository emitterRepository;
	private final SseNotificationSender sender;

	public SseEmitter connect(Long userId) {
		String emitterId = userId + "_" + System.currentTimeMillis();
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
		sender.sendNotification(emitter, emitterId, "EventStream Created. [userId=" + userId + "]", "connection");
		return emitter;
	}

}
