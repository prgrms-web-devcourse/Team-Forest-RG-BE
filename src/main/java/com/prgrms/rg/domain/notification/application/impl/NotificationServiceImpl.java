package com.prgrms.rg.domain.notification.application.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.NotificationSendFailException;
import com.prgrms.rg.domain.notification.model.SseEmitterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	private final SseEmitterRepository emitterRepository;

	public SseEmitter subscribe(Long userId, String lastEventId) {
		String emitterId = userId + "_" + System.currentTimeMillis();
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
		sendToClient(emitter, emitterId, "EventStream Created. [userId=" + userId + "]");
		if (!lastEventId.isEmpty()) {
			Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
			events.entrySet().stream()
				.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
				.forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
		}
		return emitter;
	}

	private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
		try {
			emitter.send(SseEmitter.event()
				.id(emitterId)
				.name("sse")
				.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(emitterId);
			throw new NotificationSendFailException("connect fail");
		}
	}
}
