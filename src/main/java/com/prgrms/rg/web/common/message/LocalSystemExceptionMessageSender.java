package com.prgrms.rg.web.common.message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 로컬 환경에서 예외 메시지를 로그에 출력합니다.
 */
@Slf4j
@Component
@Profile("!prod")
public class LocalSystemExceptionMessageSender implements ExceptionMessageSender {
	@Override
	public void send(Exception exception) {
		log.warn(exception.getMessage(), exception);
	}
}
