package com.prgrms.rg.domain.notification.model.sse;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.notification.model.NotificationSendFailException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SseNotificationSender {
	private final SseEmitterRepository emitterRepository;

	public void sendNotification(SseEmitter emitter, String emitterId, Object data, String eventName) {
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
