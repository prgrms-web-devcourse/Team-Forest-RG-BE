package com.prgrms.rg.infrastructure.file;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class SpyFileStoreTest {

	SpyFileStore spyFileStore = new SpyFileStore();

	@Test
	@DisplayName("이미지 저장 요청이 전송되었음을 확인할 수 있다.")
	void validate_saving_data() {

		// Given
		var file = new MockMultipartFile("image.jpg", "<<image>>".getBytes());
		var filename = "image.jpg";

		// When
		var savedUrl = spyFileStore.save(file, filename);

		// Then
		assertThatNoException().isThrownBy(()-> spyFileStore.assertFileStored(savedUrl));

	}
	@Test
	@DisplayName("이미지가 삭제 요청이 전달되었음을 확인할 수 있다.")
	void validate_remo_data() {

		// Given
		var file = new MockMultipartFile("image.jpg", "<<image>>".getBytes());
		var filename = "image.jpg";
		var savedUrl = spyFileStore.save(file, filename);

		// When
		spyFileStore.delete(savedUrl);

		// Then
		assertThatNoException().isThrownBy(() -> spyFileStore.assertFileDeleted(savedUrl));

	}
}
