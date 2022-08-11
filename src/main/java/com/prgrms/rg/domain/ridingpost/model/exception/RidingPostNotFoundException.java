package com.prgrms.rg.domain.ridingpost.model.exception;

import java.util.NoSuchElementException;

public class RidingPostNotFoundException extends NoSuchElementException {
	public RidingPostNotFoundException(Long id) {
		super("유효하지 않은 게시글 id : '" + id + "'");
	}

}
