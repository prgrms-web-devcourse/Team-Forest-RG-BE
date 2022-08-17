package com.prgrms.rg.web.file.api;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.common.file.application.AsyncImageManager;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.testutil.ControllerTest;

@AutoConfigureRestDocs
@ControllerTest(controllers = ImageRestControllerV1.class)
class ImageRestControllerV1Test {

	@MockBean
	private AsyncImageManager imageManager;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("이미지 저장 테스트")
	void storeImageTest() throws Exception {

		var image = new MockMultipartFile("image", "test-image.png", "image/png", "test".getBytes());
		var tempImage = new TemporaryImage("test-image.png", "http://test.png");

		when(imageManager.store(image)).thenReturn(tempImage);

		mockMvc.perform(RestDocumentationRequestBuilders.multipart("/api/v1/images").file(image))
			.andExpect(status().isOk())
			.andDo(document("image-upload",
					requestParts(
						partWithName("image").description("업로드할 이미지")
					),
					responseFields(
						fieldWithPath("id").description("저장된 이미지의 ID"),
						fieldWithPath("originalFileName").description("저장된 이미지의 본 파일명"),
						fieldWithPath("url").description("저장된 이미지의 url")
					)
				)
			)
			.andDo(print());
	}

	@Test
	@DisplayName("저장된 이미지의 삭제 테스트")
	void deleteImageTest() throws Exception {

		var imageId = 10L;

		doNothing().when(imageManager).delete(imageId);

		mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/images/{imageId}", imageId))
			.andExpect(status().isOk())
			.andDo(document("image-delete",
				pathParameters(
					parameterWithName("imageId").description("삭제할 이미지 ID")
				)
			))
			.andDo(print());

	}

}