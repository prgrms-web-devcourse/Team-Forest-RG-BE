package com.prgrms.rg.web.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GlobalServerErrorResult> handleValidationException(MethodArgumentNotValidException e,
		HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		BindingResult bindingResult = e.getBindingResult();
		bindingResult.getFieldErrors().forEach(
			(error) -> sb.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("\n"));
		bindingResult.getGlobalErrors().forEach(
			(error) -> sb.append(error.getDefaultMessage()).append("\n"));
		globalMessageSender.send(sb.toString(), request);
		return ResponseEntity.badRequest().body(GlobalServerErrorResult.from(sb.toString()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalServerErrorResult> handleUnhandledException(Exception exception,
		HttpServletRequest request) {
		globalMessageSender.send(exception, request);
		return ResponseEntity.internalServerError().body(GlobalServerErrorResult.INTERNAL_SERVER_ERROR);
	}

	// TODO : 나중에 WebSecuriyConfigure에서 따로 AccessDeniedHandler 처리하기
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<GlobalServerErrorResult> handleAccessDeniedException(AccessDeniedException exception,
		HttpServletRequest request) {
		globalMessageSender.send(exception, request);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GlobalServerErrorResult.ACCESS_DENIED);
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<GlobalServerErrorResult> handleUnauthorizedException(UnAuthorizedException exception) {
		log.info(exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(GlobalServerErrorResult.from(exception.getMessage()));
	}

	@ExceptionHandler(AsyncRequestTimeoutException.class)
	public void handleTimeOutException(AsyncRequestTimeoutException e) {
		log.info("SSE Connection Timeout");
	}

}
