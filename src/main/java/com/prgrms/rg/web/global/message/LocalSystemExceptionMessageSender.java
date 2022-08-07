package com.prgrms.rg.web.global.message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!prod")
public class LocalSystemExceptionMessageSender implements ExceptionMessageSender {
	@Override
	public void send(Exception exception) {
		log.warn(exception.getMessage(), exception);
	}
}
