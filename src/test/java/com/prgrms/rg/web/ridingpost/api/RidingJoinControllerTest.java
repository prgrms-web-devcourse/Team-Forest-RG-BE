package com.prgrms.rg.web.ridingpost.api;

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

import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.ridingpost.application.impl.RidingJoinService;
import com.prgrms.rg.testutil.ControllerTest;

@ControllerTest(controllers = RidingJoinController.class)
@AutoConfigureMockMvc
class RidingJoinControllerTest {
	@Autowired
	JwtTokenProvider tokenProvider;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RidingJoinService joinService;

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
}