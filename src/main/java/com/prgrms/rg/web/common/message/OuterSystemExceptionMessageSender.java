package com.prgrms.rg.web.common.message;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 배포 환경 예외 메시지를 외부 시스템에 전송합니다.
 */
@Component
@Profile("prod")
@Slf4j
public class OuterSystemExceptionMessageSender implements ExceptionMessageSender {
	private  void sendMessage(HttpServletRequest request, String message) {
		try {
			String prefixMessage = MessageFactory.createHttpMessage(request);
			CriticalMessageSender.send(prefixMessage + "\n" + message);
		} catch (Exception messageException) {
			log.error(messageException.getMessage(), messageException);
		}
	}

	@Override
	public void send(Exception exception, HttpServletRequest request) {
		sendMessage(request, MessageFactory.createStackTraceMessage(exception));
	}

	@Override
	public void send(String message, HttpServletRequest request) {
		sendMessage(request, MessageFactory.createStringMessage(message));
	}
}
