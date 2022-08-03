package com.prgrms.rg.infrastructure.file;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.application.exception.FileIOException;
import com.prgrms.rg.domain.common.file.application.FileStore;

/**
 * FileStore Dummy 클래스
 * 직접 사용하시면 안 됩니다!
 * 실제 FileStore 인터페이스에 대한 상호작용 검증은 @MockBean을 통해 FileStore Mock을 DI받아서
 * Mockito API를 사용하셔야 합니다!
 */
@Component
@Primary
public class FileStoreDummy implements FileStore {

	@Override
	public String save(MultipartFile multipartFile, String fileName) throws FileIOException {
		return fileName;
	}

	@Override
	public void delete(String fileUrl) {
	}

}
