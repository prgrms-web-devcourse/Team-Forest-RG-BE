package com.prgrms.rg.web.ridingpost.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.ridingpost.application.impl.RidingJoinService;
import com.prgrms.rg.domain.ridingpost.model.exception.CancelDeadlineOverException;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.web.common.results.ErrorResult;

@ControllerTest(controllers = RidingJoinController.class)
@AutoConfigureMockMvc
class RidingJoinControllerTest {
	@Autowired
	JwtTokenProvider tokenProvider;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RidingJoinService joinService;
	@Autowired
	ObjectMapper mapper;

	@Test
	@DisplayName("라이딩 참가 신청 - 정상 처리")
	void joinRiding() throws Exception {
		//given
		doNothing().when(joinService).joinUserToRiding(anyLong(), anyLong());
		String token = tokenProvider.createToken("ROLE_USER", 1L);
		//when
		mockMvc.perform(post("/api/v1/ridingposts/25/join")
				.header(HttpHeaders.AUTHORIZATION, "token " + token))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
	}

	@Test
	@DisplayName("라이딩 참가 신청 취소 - 정상 처리")
	void cancelJoin() throws Exception {
		//given
		doNothing().when(joinService).cancelJoin(anyLong(), anyLong());
		String token = tokenProvider.createToken("ROLE_USER", 1L);
		//when
		mockMvc.perform(post("/api/v1/ridingposts/25/cancel")
				.header(HttpHeaders.AUTHORIZATION, "token " + token))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
	}

	@Test
	@DisplayName("라이딩 참가 신청 취소 - 라이딩 마감 시간 이후 취소 요청일 경우 코드 0 전송")
	void cancelFail() throws Exception {
		//given
		doThrow(new CancelDeadlineOverException("deadline over")).when(joinService).cancelJoin(anyLong(), anyLong());
		String token = tokenProvider.createToken("ROLE_USER", 1L);
		//when
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/ridingposts/25/cancel")
				.header(HttpHeaders.AUTHORIZATION, "token " + token))
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof CancelDeadlineOverException))
			.andDo(print())
			.andReturn();

		String responseString = mvcResult.getResponse().getContentAsString();
		ErrorResult errorResult = mapper.readValue(responseString, ErrorResult.class);
		assertThat(errorResult.getErrorCode()).isEqualTo(RidingJoinExceptionHandler.CANCEL_DEADLINE_EXCEPTION_CODE);
	}
}