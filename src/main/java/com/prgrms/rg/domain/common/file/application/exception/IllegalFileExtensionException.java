package com.prgrms.rg.domain.common.file.application.exception;

public class
IllegalFileExtensionException extends IllegalArgumentException {
	public IllegalFileExtensionException(String extension) {
		super("확장자 " + extension + " 은 지원하지 않습니다.");
	}
}
