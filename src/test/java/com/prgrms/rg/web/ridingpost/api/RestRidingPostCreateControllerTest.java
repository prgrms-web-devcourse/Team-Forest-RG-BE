package com.prgrms.rg.web.ridingpost.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.web.ridingpost.requests.RidingCreateMainRequest;
import com.prgrms.rg.web.ridingpost.requests.RidingPostCreateRequest;

@ControllerTest(controllers = RestRidingPostCreateController.class)
@AutoConfigureMockMvc
class RestRidingPostCreateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RidingPostService ridingPostService;

	@Autowired
	JwtTokenProvider tokenProvider;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	@DisplayName("RidingPost 생성 테스트")
	void createRidingTest() throws Exception {

		//given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var body = new RidingPostCreateRequest(
			new RidingCreateMainRequest("title", LocalDateTime.now().plusDays(10L),
				"2시간 30분", List.of("start", "end"), 10000, 5, 20,
				10001, new Coordinate(0, 0), "상", List.of("MTB", "로드"), null), Collections.emptyList());

		var bodyString = objectMapper.writeValueAsString(body);
		//when
		when(ridingPostService.createRidingPost(1L, body.toCommand())).thenReturn(2L);

		mockMvc.perform(post("/ridingposts")
			.header("Authorization", "token " + token)
			.content(bodyString)
			.contentType("application/json")
		).andExpect(status().isOk())
			.andDo(print());

	}

	@Test
	@DisplayName("ridingpost 생성 실패 테스트 - 잘못된 입력")
	void handlerIllegalExceptionTest() throws Exception {

	    //given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var body = new RidingPostCreateRequest(
			new RidingCreateMainRequest("error-title", LocalDateTime.now().plusDays(10L),
				"12321???", List.of("start", "end"), 10000, 5, 20,
				10001, new Coordinate(0, 0), "모름", List.of("MTB", "로드"), null), Collections.emptyList());

		var bodyString = objectMapper.writeValueAsString(body);

		//when
		when(ridingPostService.createRidingPost(anyLong(), any(RidingCreateCommand.class))).thenThrow(new IllegalArgumentException());

	    //then
		mockMvc.perform(post("/ridingposts")
				.header("Authorization", "token " + token)
				.content(bodyString)
				.contentType("application/json")
			).andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
			.andDo(print());
	}

}