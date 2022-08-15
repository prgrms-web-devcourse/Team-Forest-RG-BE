package com.prgrms.rg.web.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.prgrms.rg.domain.user.application.exception.DuplicateNicknameException;
import com.prgrms.rg.web.common.RgControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RgControllerAdvice(assignableTypes = UserController.class)
@Slf4j
public class UserControllerAdvice {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateNicknameException.class)
	public String handleDuplicateNicknameException(DuplicateNicknameException e) {
		log.info(e.getMessage(), e);
		return "다른 유저가 사용중인 닉네임을 사용할 수는 없습니다.";
	}
}
