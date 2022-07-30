package com.prgrms.rg.domain.notification.application.impl;

import java.io.IOException;

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

	public SseEmitter subscribe(String userId, String lastEventId) {
		String emitterId = userId + "_" + System.currentTimeMillis();
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
		sendEvent(emitter, emitterId, "EventStream Created. [userId=" + userId + "]","connection");
		return emitter;
	}

	private void sendEvent(SseEmitter emitter, String emitterId, Object data, String eventName) {
		try {
			emitter.send(SseEmitter.event()
				.id(emitterId)
				.name(eventName)
				.data(data));
		} catch (IOException e) {
			emitterRepository.deleteById(emitterId);
			throw new NotificationSendFailException(e);
		}
	}
}
