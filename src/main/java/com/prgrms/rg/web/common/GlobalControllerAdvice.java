package com.prgrms.rg.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.web.common.message.ExceptionMessageSender;
import com.prgrms.rg.web.common.results.GlobalServerErrorResult;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

	private final ExceptionMessageSender globalMessageSender;

	public GlobalControllerAdvice(ExceptionMessageSender globalMessageSender) {
		this.globalMessageSender = globalMessageSender;
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalServerErrorResult> handleUnhandledException(Exception exception) {
		globalMessageSender.send(exception);
		return ResponseEntity.internalServerError().body(GlobalServerErrorResult.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<GlobalServerErrorResult> handleUnauthorizedException(UnAuthorizedException exception) {
		log.info(exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GlobalServerErrorResult.from(exception.getMessage()));
	}

}
