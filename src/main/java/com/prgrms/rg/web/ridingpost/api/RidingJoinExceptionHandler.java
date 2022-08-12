package com.prgrms.rg.web.ridingpost.api;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.domain.ridingpost.model.exception.CancelDeadlineOverException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinCancelFailException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinFailException;
import com.prgrms.rg.web.common.results.ErrorResult;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@Order(500)
public class RidingJoinExceptionHandler {
	private static final String LOG_MARKER = "[RidingJoinExceptionHandler]";
	public static final Integer CANCEL_DEADLINE_EXCEPTION_CODE = 0;
	public static final Integer INVALID_ARGUMENT_EXCEPTION_CODE = 1;

	@ExceptionHandler({RidingJoinFailException.class, RidingJoinCancelFailException.class})
	public ResponseEntity<ErrorResult> handleRidingJoinFailException(RuntimeException e) {
		log.info(LOG_MARKER, e);
		ErrorResult errorResult = new ErrorResult(INVALID_ARGUMENT_EXCEPTION_CODE, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
	}

	@ExceptionHandler(CancelDeadlineOverException.class)
	public ResponseEntity<ErrorResult> handleCancelDeadlineOverException(CancelDeadlineOverException e) {
		log.info(LOG_MARKER, e);
		ErrorResult errorResult = new ErrorResult(CANCEL_DEADLINE_EXCEPTION_CODE, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
	}

}
