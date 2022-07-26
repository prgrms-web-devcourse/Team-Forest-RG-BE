package com.prgrms.rg.domain.notification.model.sse;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.notification.model.NotificationSendFailException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseNotificationSender {
	private final SseEmitterRepository emitterRepository;

	public void sendNotification(Long receiverId, Object data, String eventName) {
		Map<String, SseEmitter> emitterMap = emitterRepository.findEmitterByUserId(receiverId);
		String emitterId = receiverId.toString();
		SseEmitter emitter = emitterMap.get(emitterId);
		log.info("health-check in progress");
		try {
			emitter.send(SseEmitter.event()
				.id(emitterId)
				.name(eventName)
				.data(data));
			log.info("health-check done");
		} catch (IOException e) {
			emitterRepository.deleteById(emitterId);
			throw new NotificationSendFailException(e);
		}
	}
}
