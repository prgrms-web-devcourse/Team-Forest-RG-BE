package com.prgrms.rg.domain.common.file.application;

public class EmptyFileException extends RuntimeException {
	public EmptyFileException() {
		super("비어 있는 파일입니다.");
	}
}
