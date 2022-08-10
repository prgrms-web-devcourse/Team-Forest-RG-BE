package com.prgrms.rg.web.ridingpost.api;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1000)
@RestControllerAdvice(assignableTypes = RidingPostSaveController.class)
public class RidingSaveExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {

		log.warn("ILLEGAL ARGUMENT", e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<String> handleUnAuthorized(UnAuthorizedException e) {

		log.warn("UNAUTHORIZED ACCESS", e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
}
