package com.prgrms.rg.domain.ridingpost.model;

public class RidingPostNotFoundException extends RuntimeException {
	public RidingPostNotFoundException(Long id) {
		super("유효하지 않은 게시글 id : '" + id + "'");
	}

}
