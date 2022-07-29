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
 * FileStore를 검증하고 싶을 경우 @Autowired를 할 때 SpyFileStore를 선언해야 아래의 검증 메서드를 사용할 수 있습니다.
 * 검증 사용이 불편하면 이 파일을 무시하시고 일반적인 스프링 테스트처럼 @MockBean을 사용하셔도 됩니다.
 */
@Component
public class SpyFileStore implements FileStore {

	String prefix = "https://rg.storage.url/";


	Map<String, Integer> saveCommandCache = new HashMap<>();
	Map<String, Integer> deleteCommandCache = new HashMap<>();

	/*
	 * 이미지 전송 요청이 제대로 전달 되었는가 확인하는 메서드
	 */
	public void assertSaveCommandCalledOnce(String url) {
		assertThat(saveCommandCache).containsEntry(url, 1);
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
		if(saveCommandCache.containsKey(url)) {
			saveCommandCache.put(url, saveCommandCache.get(url) + 1);
			return url;
		}
		saveCommandCache.put(url, 1);
		return url;
	}

	@Override
	public void delete(String fileUrl) {
		if(deleteCommandCache.containsKey(fileUrl)) {
			deleteCommandCache.put(fileUrl, deleteCommandCache.get(fileUrl) + 1);
			return;
		}
		deleteCommandCache.put(fileUrl, 1);
	}
}
