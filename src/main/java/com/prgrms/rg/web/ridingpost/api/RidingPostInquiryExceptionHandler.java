package com.prgrms.rg.web.ridingpost.api;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@Order(1)
public class RidingPostInquiryExceptionHandler {

	@ExceptionHandler(RidingPostNotFoundException.class)
	public ResponseEntity<String> handleRidingPostNotFoundException(RidingPostNotFoundException e) {
		log.info(e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
