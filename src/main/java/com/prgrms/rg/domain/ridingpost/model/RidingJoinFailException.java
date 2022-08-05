package com.prgrms.rg.domain.ridingpost.model;

public class RidingJoinFailException extends RuntimeException {
	public RidingJoinFailException(String message) {
		super(message);
	}

	public RidingJoinFailException(Throwable cause) {
		super(cause);
	}
}
