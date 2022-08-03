package com.prgrms.rg.domain.ridingpost.model;

public class RidingPostNotFoundException extends RuntimeException {
	public RidingPostNotFoundException(Long id) {
		super("cannot find RidingPost id: '" + id + "'");
	}

}
