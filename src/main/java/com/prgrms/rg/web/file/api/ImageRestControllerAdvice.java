package com.prgrms.rg.web.file.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.rg.domain.common.file.application.exception.EmptyFileException;
import com.prgrms.rg.domain.common.file.application.exception.FileIOException;
import com.prgrms.rg.domain.common.file.application.exception.IllegalFileExtensionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(assignableTypes = ImageRestController.class)
public class ImageRestControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public String handleEmptyFileException(EmptyFileException e) {
		log.info(e.getMessage(), e);
		return "비어 있는 파일이기에 저장이 불가합니다.";
	}

	//TODO: 알림 전송하거나, 다른 핸들러로 전송
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	public String handleFileIOException(FileIOException e) {
		log.info(e.getMessage(), e);
		return "파일을 저장하는 과정에서 입출력 에러가 발생하였습니다.";
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler
	public String handleIllegalFileExtensionException(IllegalFileExtensionException e) {
		log.info(e.getMessage(), e);
		return "지원하지 않는 파일 확장자입니다.";
	}
}