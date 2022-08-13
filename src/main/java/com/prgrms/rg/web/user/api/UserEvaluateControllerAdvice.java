package com.prgrms.rg.web.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.user.application.exception.EvaluationFailException;
import com.prgrms.rg.web.common.RgControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RgControllerAdvice(assignableTypes = UserEvaluateController.class)
public class UserEvaluateControllerAdvice {

	@ExceptionHandler(value = EvaluationFailException.class)
	public ResponseEntity<String> handleEvaluationFailed(EvaluationFailException e) {
		log.warn("Evaluation Failed.", e);

		return new ResponseEntity<>("Cannot Evaluate.", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = UnAuthorizedException.class)
	public ResponseEntity<String> handleUnAuthorized(UnAuthorizedException e) {
		log.warn("UnAuthorized Request.", e);

		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(value = RidingPostNotFoundException.class)
	public ResponseEntity<String> handleNotFoundPost(RidingPostNotFoundException e) {
		log.warn("Cannot find post", e);

		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
