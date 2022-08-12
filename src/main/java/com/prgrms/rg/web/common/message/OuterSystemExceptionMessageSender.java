package com.prgrms.rg.web.common.message;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 배포 환경 예외 메시지를 외부 시스템에 전송합니다.
 */
@Component
@Profile("prod")
@Slf4j
public class OuterSystemExceptionMessageSender implements ExceptionMessageSender {
	@Override
	public void send(Exception exception, HttpServletRequest request) {
		log.warn(exception.getMessage(), exception);
		try {
			String prefixMessage = MessageFactory.createHttpMessage(request);
			String bodyMessage = MessageFactory.createStackTraceMessage(exception);
			CriticalMessageSender.send(prefixMessage + "\n" + bodyMessage);
		} catch (Exception messageException) {
			log.error(messageException.getMessage(), messageException);
		}
	}

	@Override
	public void send(String message, HttpServletRequest request) {
		log.warn(message);
		try {
			String prefixMessage = MessageFactory.createHttpMessage(request);
			String bodyMessage = MessageFactory.createStringMessage(message);
			CriticalMessageSender.send(prefixMessage + "\n" + bodyMessage);
		} catch (Exception messageException) {
			log.error(messageException.getMessage(), messageException);
		}
	}
}
