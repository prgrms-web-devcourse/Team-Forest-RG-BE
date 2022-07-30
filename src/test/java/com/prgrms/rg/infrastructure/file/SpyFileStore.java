package com.prgrms.rg.infrastructure.file;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.rg.domain.common.file.FileIOException;
import com.prgrms.rg.domain.common.file.FileStore;

/**
 * FileStore 테스트용 spy 클래스
 * 테스트 환경에서 운영용 FileStore 구현체 대신 DI 됩니다.
 * 동작을 검증하고 싶을 경우 @Autowired를 할 때 SpyFileStore 타입으로 선언해야 아래의 검증 메서드를 사용할 수 있습니다.
 * 검증 사용이 불편하면 이 파일을 무시하시고 일반적인 스프링 테스트처럼 @MockBean을 통해 Mockito 방식으로 FileStore를 테스트 하시면 됩니다.
 */
@Component
public class SpyFileStore implements FileStore {

	String prefix = "https://rg.storage.url/";

	Map<String, Integer> saveCommandCache = new HashMap<>();
	Map<String, Integer> deleteCommandCache = new HashMap<>();

	/*
	 * 이미지 전송 요청이 제대로 전달 되었는가 확인하는 메서드
	 */
	public void assertSaveCommandCalledOnce(MultipartFile savedFile, String savedFileName) {
		assertThat(saveCommandCache).containsEntry(hashToCommandKey(savedFileName, savedFile), 1);
	}

	/*
	 * 이미지 삭제 요청이 제대로 전달 되었는가 확인하는 메서드
	 */
	public void assertDeleteCommandCalledOnce(String url) {
		assertThat(deleteCommandCache).containsEntry(url, 1);
	}

	@Override
	public String save(MultipartFile multipartFile, String fileName) throws FileIOException {
		assertThat(multipartFile).isNotNull();
		var url = prefix + fileName;
		var key = hashToCommandKey(fileName, multipartFile);
		saveCommandCache.put(key, saveCommandCache.getOrDefault(key, 0) + 1);
		return url;
	}

	@Override
	public void delete(String fileUrl) {
		deleteCommandCache.put(fileUrl, deleteCommandCache.getOrDefault(fileUrl, 0) + 1);
	}

	private String hashToCommandKey(String fileName, MultipartFile multipartFile) {
		return fileName + " " + multipartFile.toString();
	}

}
