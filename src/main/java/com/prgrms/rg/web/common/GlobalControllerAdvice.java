package com.prgrms.rg.web.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.web.common.message.ExceptionMessageSender;
import com.prgrms.rg.web.common.results.InternalServerErrorResult;

@RestControllerAdvice
public class GlobalControllerAdvice {

	private final ExceptionMessageSender globalMessageSender;

	public GlobalControllerAdvice(ExceptionMessageSender globalMessageSender) {
		this.globalMessageSender = globalMessageSender;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<InternalServerErrorResult> handleUnhandledException(Exception exception) {
		globalMessageSender.send(exception);
		return ResponseEntity.internalServerError().body(InternalServerErrorResult.create());
	}

}
