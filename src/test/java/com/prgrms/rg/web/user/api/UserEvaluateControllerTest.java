package com.prgrms.rg.web.user.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.user.application.UserEvaluateService;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.web.user.requests.ParticipantEvaluateRequest;
import com.prgrms.rg.web.user.requests.RidingEvaluateRequest;

@ControllerTest(controllers = UserEvaluateController.class)
class UserEvaluateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserEvaluateService evaluateService;

	@Autowired
	JwtTokenProvider tokenProvider;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("평가 controller 테스트")
	void evaluateTest() throws Exception{

		Long postId = 10L;
		String token = tokenProvider.createToken("ROLE_USER", 1L);
		var request = new RidingEvaluateRequest(postId, List.of(
			new ParticipantEvaluateRequest(5L, false, true),
			new ParticipantEvaluateRequest(6L, false, false)
		));

		doNothing().when(evaluateService).evaluateMembers(1L, postId,
			request.getEvaluatedMemberList().stream().map(
				ParticipantEvaluateRequest::toCommand).collect(
				Collectors.toList()));

		mockMvc.perform(post("/api/v1/user/evaluate")
				.header("Authorization", "token " + token)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request))
			).andExpect(status().isOk())
			.andExpect(content().string(containsString("evaluate 성공")))
			.andDo(print());
	}
}