package com.prgrms.rg.domain.common.file.application;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.application.exception.FileIOException;

public interface FileStore {
	/**
	 * 파일을 저장합니다.
	 * @param multipartFile: 저장할 파일입니다.
	 * @param fileName: 파일 저장 시에 사용할 이름입니다.
	 * @return 파일 url을 반환합니다.
	 */
	String save(MultipartFile multipartFile, String fileName) throws FileIOException;

	/**
	 * 파일을 삭제합니다.
	 * @param fileUrl 해당 파일의 url입니다.
	 */
	void delete(String fileUrl);
}
