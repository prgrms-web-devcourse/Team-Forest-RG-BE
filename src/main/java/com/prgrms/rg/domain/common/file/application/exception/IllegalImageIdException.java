package com.prgrms.rg.domain.common.file.application.exception;

public class IllegalImageIdException extends RuntimeException {
	public IllegalImageIdException(Long id) {
		super(id + "에 해당하는 이미지 정보가 확인되지 않습니다. 잘못된 이미지 id 값을 전달받았습니다.");
	}
}
