package com.prgrms.rg.web.ridingpost.api;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@ControllerTest(controllers = RidingInquiryController.class)
@AutoConfigureMockMvc
class RidingInquiryControllerTest {
	@Autowired
	JwtTokenProvider tokenProvider;
	@MockBean
	RidingPostReadService readService;
	@Autowired
	RidingInquiryController controller;
	@Autowired
	ObjectMapper mapper;
	Long userId = 1L;
	Long postId = 10L;
	RidingPost ridingPost;
	@Autowired
	private MockMvc mockMvc;

	@DisplayName("라이딩 게시글 상세 조회 정상 요청")
	@Test
	void test() throws Exception {
		//given
		var token = tokenProvider.createToken("ROLE_USER", 1L);
		ridingPost = TestEntityDataFactory.createRidingPost(userId);

		RidingPostInfo info = RidingPostInfo.from(ridingPost);
		when(readService.getRidingPostInfoById(postId)).thenReturn(info);
		//when
		MvcResult result = mockMvc.perform(get("/api/v1/ridingposts/" + postId)
				.header("Authorization", "token " + token))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}

	@DisplayName("잘못된 id로 라이딩 게시글 상세 조회")
	@Test
	void failTest() throws Exception {
		//given
		var token = tokenProvider.createToken("ROLE_USER", 1L);
		Long invalidId = 99999L;
		when(readService.getRidingPostInfoById(invalidId)).thenThrow(new RidingPostNotFoundException(invalidId));

		//when
		mockMvc.perform(get("/api/v1/ridingposts/" + invalidId)
				.header("Authorization", "token " + token))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof RidingPostNotFoundException));
	}
}