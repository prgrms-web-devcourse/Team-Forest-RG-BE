package com.prgrms.rg.domain.user.application;

import java.util.NoSuchElementException;

public class NoSuchUserException extends NoSuchElementException {
	public NoSuchUserException(Long id) {
		super("id:" + id + "에 해당하는 User를 찾지 못함");
	}
}
