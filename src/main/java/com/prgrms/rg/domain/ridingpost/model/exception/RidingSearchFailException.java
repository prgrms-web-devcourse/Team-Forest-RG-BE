package com.prgrms.rg.domain.ridingpost.model.exception;

public class RidingSearchFailException extends RuntimeException {
	public RidingSearchFailException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
