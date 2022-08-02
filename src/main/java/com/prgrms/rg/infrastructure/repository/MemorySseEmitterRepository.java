package com.prgrms.rg.infrastructure.repository;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.prgrms.rg.domain.notification.model.sse.SseEmitterRepository;

@Component
public class MemorySseEmitterRepository implements SseEmitterRepository {
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	@Override
	public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
		emitters.put(emitterId, sseEmitter);
		return sseEmitter;
	}

	@Override
	public void saveEventCache(String eventCacheId, Object event) {
		eventCache.put(eventCacheId, event);
	}

	@Override
	public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
		return emitters.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(memberId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public Map<String, SseEmitter> findEmitterByUserId(Long userId) {
		SseEmitter emitter = emitters.get(userId.toString());
		return Collections.singletonMap(userId.toString(), emitter);
	}

	@Override
	public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
		return eventCache.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(memberId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public void deleteById(String id) {
		emitters.remove(id);
	}

	@Override
	public void deleteAllEmitterStartWithId(String memberId) {
		emitters.forEach(
			(key, emitter) -> {
				if (key.startsWith(memberId)) {
					emitters.remove(key);
				}
			}
		);
	}

	@Override
	public void deleteAllEventCacheStartWithId(String memberId) {
		eventCache.forEach(
			(key, emitter) -> {
				if (key.startsWith(memberId)) {
					eventCache.remove(key);
				}
			}
		);
	}
}
