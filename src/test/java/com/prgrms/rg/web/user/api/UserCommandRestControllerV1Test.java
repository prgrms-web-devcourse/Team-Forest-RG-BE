package com.prgrms.rg.web.user.api;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.user.application.UserCommandService;
import com.prgrms.rg.domain.user.application.command.UserUpdateCommand;
import com.prgrms.rg.domain.user.application.exception.NoSuchUserException;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.web.user.requests.UserUpdateRequest;

@AutoConfigureRestDocs
@ControllerTest(controllers = UserCommandRestControllerV1.class)
class UserCommandRestControllerV1Test {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserCommandService userCommandService;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("User 정보 수정 테스트")
	void updateUserTest() throws Exception {
		//Given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var request = new UserUpdateRequest("RG라이더", 5, "하",
			new String[] {"MTB", "로드"}, "잘 부탁드립니다. 한강 라이딩을 즐겨 합니다.", 110, "01012345678", 3L);

		var command = request.toCommand(1L);

		var body = objectMapper.writeValueAsString(request);

		when(userCommandService.edit(command, 1L)).thenReturn(1L);

		//When
		mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/users/{userId}", 1L)
				.header("Authorization", "token " + token)
				.content(body)
				.contentType("application/json"))
			.andExpect(status().isOk())
			.andDo(document("user-update",
				pathParameters(
					parameterWithName("userId").description("수정할 유저 ID")
				),
				requestFields(
					fieldWithPath("nickname").description("유저 닉네임"),
					fieldWithPath("ridingStartYear").description("라이딩 시작 년도"),
					fieldWithPath("ridingLevel").description("라이딩 레벨"),
					fieldWithPath("bicycles[]").description("자전거 리스트"),
					fieldWithPath("introduction").description("자기소개"),
					fieldWithPath("favoriteRegionCode").description("자신의 지역 코드"),
					fieldWithPath("phoneNumber").description("전화번호"),
					fieldWithPath("profileImageId").description("프로필 이미지 ID")
				),
				responseBody()
				))
			.andDo(print());
	}

	@Test
	@DisplayName("User 정보 수정 - 잘못된 요청")
	void updateUserTest_fail_with_Argument() throws Exception {
		//Given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var request = new UserUpdateRequest("", 5, "",
			new String[] {"MTB", "로드"}, "잘 부탁드립니다. 한강 라이딩을 즐겨 합니다.", 110, "01012345678", null);
		var command = request.toCommand(1L);

		var body = objectMapper.writeValueAsString(request);

		when(userCommandService.edit(command, 1L)).thenReturn(1L);

		//When
		mockMvc.perform(put(("/api/v1/users/1"))
				.header("Authorization", "token " + token)
				.content(body)
				.contentType("application/json")
			).andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("User 정보 수정 실패 - 잘못된 ID로 요청")
	void request_with_invalid_id() throws Exception {

		//Given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var request = new UserUpdateRequest("RG라이더", 5, "하",
			new String[] {"MTB", "로드"}, "잘 부탁드립니다. 한강 라이딩을 즐겨 합니다.", 110, "01012345678", null);

		UserUpdateCommand command = request.toCommand(1L);

		var body = objectMapper.writeValueAsString(request);

		when(userCommandService.edit(command, 1L)).thenThrow(new NoSuchUserException(1L));

		//When
		mockMvc.perform(put(("/api/v1/users/1"))
				.header("Authorization", "token " + token)
				.content(body)
				.contentType("application/json")
			).andExpect(status().isBadRequest())
			.andDo(print());
	}
}