package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinFailException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RidingJoinExceptionHandler {
	private static final String LOG_MARKER = "[RidingJoinExceptionHandler]";

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(RidingJoinFailException.class)
	public ResponseEntity<String> handleRidingJoinFailException(RidingJoinFailException e) {
		log.info(LOG_MARKER, e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

}
