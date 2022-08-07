package com.prgrms.rg.web.common.message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.prgrms.rg.infrastructure.cloud.CriticalMessageSender;

import lombok.extern.slf4j.Slf4j;

/**
 * 배포 환경 예외 메시지를 외부 시스템에 전송합니다.
 */
@Component
@Profile("prod")
@Slf4j
public class OuterSystemExceptionMessageSender implements ExceptionMessageSender {
	@Override
	public void send(Exception exception) {
		log.warn(exception.getMessage(), exception);
		try {
			CriticalMessageSender.send(exception);
		} catch (Exception messageException) {
			log.error(messageException.getMessage(), messageException);
		}
	}
}
