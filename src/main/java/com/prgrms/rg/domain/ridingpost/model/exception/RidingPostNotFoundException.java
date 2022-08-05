package com.prgrms.rg.domain.ridingpost.model.exception;

public class RidingPostNotFoundException extends RuntimeException {
	public RidingPostNotFoundException(Long id) {
		super("cannot find RidingPost id: '" + id + "'");
	}

}
