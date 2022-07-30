package com.prgrms.rg.domain.notification.application;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;



public interface NotificationService {
	SseEmitter subscribe(String userId, String lastEventId);

}
