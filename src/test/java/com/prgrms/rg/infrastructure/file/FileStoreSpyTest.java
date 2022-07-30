package com.prgrms.rg.infrastructure.file;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class FileStoreSpyTest {

	FileStoreSpy fileStoreSpy = new FileStoreSpy();

	@Test
	@DisplayName("이미지 저장 요청이 전송되었음을 확인할 수 있다.")
	void validate_saving_data() {

		// Given
		var file = new MockMultipartFile("image.jpg", "<<image>>".getBytes());

		var filename = "image.jpg";

		// When
		var savedUrl = fileStoreSpy.save(file, filename);
		// Then
		assertThatNoException().isThrownBy(() -> fileStoreSpy.assertSaveCommandCalledOnce(file, filename));

	}

	@Test
	@DisplayName("이미지가 삭제 요청이 전달되었음을 확인할 수 있다.")
	void validate_remo_data() {

		// Given
		var file = new MockMultipartFile("image.jpg", "<<image>>".getBytes());
		var filename = "image.jpg";
		var savedUrl = fileStoreSpy.save(file, filename);

		// When
		fileStoreSpy.delete(savedUrl);

		// Then
		assertThatNoException().isThrownBy(() -> fileStoreSpy.assertDeleteCommandCalledOnce(savedUrl));

	}
}
