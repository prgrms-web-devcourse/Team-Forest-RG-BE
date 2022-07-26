package com.prgrms.rg.web.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.domain.user.application.exception.DuplicateNicknameException;
import com.prgrms.rg.domain.user.application.exception.NoSuchUserException;
import com.prgrms.rg.web.common.RgControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RgControllerAdvice(assignableTypes = UserRestController.class)
public class UserRestControllerAdvice {

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnAuthorizedException.class)
	public String handleUnAuthorizedException(UnAuthorizedException e) {
		log.info(e.getMessage(), e);
		return e.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info(e.getMessage(), e);
		return e.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NoSuchUserException.class)
	public String handleNoSuchUserException(NoSuchUserException e) {
		log.info(e.getMessage(), e);
		return "유저 정보를 불러오는 데 실패했습니다. 적합한 id로 요청을 보내지 않았을 수 있으며, 요청을 보내는 사이에 해당 유저 정보가 삭제되었을 수 있습니다.";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateNicknameException.class)
	public String handleDuplicateNicknameException(DuplicateNicknameException e) {
		log.info(e.getMessage(), e);
		return "다른 유저가 사용중인 닉네임을 사용할 수는 없습니다.";
	}
}