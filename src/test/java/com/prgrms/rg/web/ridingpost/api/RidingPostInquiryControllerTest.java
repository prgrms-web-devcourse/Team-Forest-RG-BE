package com.prgrms.rg.web.ridingpost.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingSearchFailException;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@ControllerTest(controllers = RidingPostInquiryController.class)
@AutoConfigureMockMvc
class RidingPostInquiryControllerTest {
	@Autowired
	JwtTokenProvider tokenProvider;
	@MockBean
	RidingPostReadService readService;
	@Autowired
	RidingPostInquiryController controller;
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
		ridingPost = TestEntityDataFactory.createRidingPost(userId);
		RidingPostInfo info = RidingPostInfo.from(ridingPost);
		when(readService.loadRidingPostInfoById(postId)).thenReturn(info);
		//when
		MvcResult result = mockMvc.perform(get("/api/v1/ridingposts/" + postId))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
	}

	@DisplayName("잘못된 id로 라이딩 게시글 상세 조회")
	@Test
	void failTest() throws Exception {
		//given
		Long invalidId = 99999L;
		when(readService.loadRidingPostInfoById(invalidId)).thenThrow(new RidingPostNotFoundException(invalidId));

		//when
		mockMvc.perform(get("/api/v1/ridingposts/" + invalidId))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof RidingPostNotFoundException));
	}

	@DisplayName("조건별 라이딩 게시글 리스트 조회 요청")
	@Test
	void test3() throws Exception {
		//given
		ridingPost = TestEntityDataFactory.createRidingPost(userId);
		RidingPostInfo info = RidingPostInfo.from(ridingPost);
		Pageable pageable = PageRequest.of(0, 50);
		Slice<RidingPostInfo> spy = new SliceImpl<>(List.of(info), pageable, false);

		//when
		when(readService.loadFilteredRidingPostByCondition(any(RidingSearchCondition.class),
			any(Pageable.class))).thenReturn(spy);
		MvcResult result = mockMvc.perform(get("/api/v1/ridingposts/")
				.param("addressCode", "11010")
				// .param("ridingStatusCode")
				.param("ridingLevel", "상")
				.param("bicycleCode", "0")
				.param("page", "0")
				.param("size", "50"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}

	@DisplayName("조건별 라이딩 게시글 리스트 조회 요청 - 조건 없이 기본값 동작 검증")
	@Test
	void test4() throws Exception {
		//given
		ridingPost = TestEntityDataFactory.createRidingPost(userId);
		RidingPostInfo info = RidingPostInfo.from(ridingPost);
		Pageable pageable = PageRequest.of(0, 50);
		Slice<RidingPostInfo> spy = new SliceImpl<>(List.of(info), pageable, false);

		//when
		when(readService.loadFilteredRidingPostByCondition(any(RidingSearchCondition.class),
			any(Pageable.class))).thenReturn(spy);
		MvcResult result = mockMvc.perform(get("/api/v1/ridingposts/"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}

	@DisplayName("조건별 라이딩 게시글 리스트 조회 요청 - 잘못된 parameter로 요청 시 RidingSearchFailException 발생, 핸들러에서 처리")
	@Test
	void test5() throws Exception {
		//given
		ridingPost = TestEntityDataFactory.createRidingPost(userId);
		RidingPostInfo info = RidingPostInfo.from(ridingPost);
		Pageable pageable = PageRequest.of(0, 50);
		Slice<RidingPostInfo> spy = new SliceImpl<>(List.of(info), pageable, false);

		//when
		when(readService.loadFilteredRidingPostByCondition(any(RidingSearchCondition.class),
			any(Pageable.class))).thenThrow(
			new RidingSearchFailException(new IllegalArgumentException("addressCode is invalid")));
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/ridingposts/")
				.param("addressCode", "-1"))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof RidingSearchFailException))
			.andReturn();

		System.out.println(mvcResult.getResolvedException().getMessage());
	}
}