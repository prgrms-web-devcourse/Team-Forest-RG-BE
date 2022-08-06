package com.prgrms.rg.web.global;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler(Exception.class)
	public void handleUnhandledException(Exception exception) {

	}

}
