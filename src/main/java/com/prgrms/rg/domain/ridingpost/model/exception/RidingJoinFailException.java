package com.prgrms.rg.domain.ridingpost.model.exception;

public class RidingJoinFailException extends RuntimeException {
	public RidingJoinFailException(String message) {
		super(message);
	}

	public RidingJoinFailException(Throwable cause) {
		super(cause);
	}
}
