package com.prgrms.rg.domain.notification.application;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;



public interface NotificationService {
	SseEmitter subscribe(Long userId, String lastEventId);

}
