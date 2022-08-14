package com.prgrms.rg.domain.notification.model.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseConnector {
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
	private final SseEmitterRepository emitterRepository;
	private final SseNotificationSender sender;

	public SseEmitter connect(Long userId) {
		String emitterId = userId.toString();
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
		log.info("SSE Emitter created successful");
		return emitter;
	}

}
