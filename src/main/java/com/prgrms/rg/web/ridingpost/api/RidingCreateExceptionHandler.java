package com.prgrms.rg.web.ridingpost.api;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1000)
@RestControllerAdvice(assignableTypes = RestRidingPostCreateController.class)
public class RidingCreateExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e) {

		log.warn("ILLEGAL ARGUMENT", e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
