package com.prgrms.rg.web.global.message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.prgrms.rg.infrastructure.cloud.CriticalMessageSender;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("prod")
@Slf4j
public class OuterSystemExceptionMessageSender implements ExceptionMessageSender {
	@Override
	public void send(Exception exception) {
		try {
			CriticalMessageSender.send(exception);
		} catch (Exception messageException) {
			log.error(messageException.getMessage(), messageException);
		}
	}
}
