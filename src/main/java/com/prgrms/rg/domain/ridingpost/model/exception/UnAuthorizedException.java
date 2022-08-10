package com.prgrms.rg.domain.ridingpost.model.exception;

public class UnAuthorizedException extends RuntimeException{

	public UnAuthorizedException(Long userId) {
		super(userId + "의 허용되지 않은 접근");
	}
}
