package com.prgrms.rg.web.user.api;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.web.user.requests.OAuthLoginRequest;
import com.prgrms.rg.web.user.requests.UserRegisterRequest;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserRegisterResult;

@ControllerTest(controllers = UserController.class)
class UserControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserAuthenticationService userAuthenticationService;

	@Autowired
	JwtTokenProvider tokenProvider;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void loginOauth_컨트롤러_정상_테스트() throws Exception {
		// 임시 사용자 생성 및 토큰 주입
		var token = tokenProvider.createToken("ROLE_USER", 3L);
		// Given
		OAuthLoginRequest oAuthLoginRequest = new OAuthLoginRequest(token);
		OAuthLoginResult oAuthLoginResult = OAuthLoginResult.of(token, false);

		String request = objectMapper.writeValueAsString(oAuthLoginRequest);
		//when
		when(userAuthenticationService.joinOAuth(token)).thenReturn(oAuthLoginResult);
		// then
		mockMvc.perform(post("/api/v1/users/oauth/login")
				.content(request)
				.contentType("application/json")
			).andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void registerUser_컨트롤러_정상_테스트() throws Exception {
		// 임시 사용자 생성 및 토큰 주입
		var token = tokenProvider.createToken("ROLE_USER", 3L);
		// Given

		List<String> bicycles = new ArrayList<>();
		bicycles.add("MTB");
		bicycles.add("Road");
		UserRegisterResult userRegisterResult = UserRegisterResult.of(true);
		UserRegisterRequest userRegisterRequest = new UserRegisterRequest(1996, 23, bicycles, "하", "010-1234-5678",
			"김훈기");

		String request = objectMapper.writeValueAsString(userRegisterRequest);
		//when
		when(userAuthenticationService.updateUserByRegistration(
			UserRegisterCommand.of(userRegisterRequest, 3L))).thenReturn(userRegisterResult);
		// then
		mockMvc.perform(post("/api/v1/users/register")
				.header("Authorization", "Bearer " + token)
				.content(request)
				.contentType("application/json")
			).andExpect(status().isOk())
			.andDo(print());
	}
}
