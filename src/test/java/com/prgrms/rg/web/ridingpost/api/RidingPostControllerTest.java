package com.prgrms.rg.web.ridingpost.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;
import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.web.ridingpost.requests.RidingSaveDetailRequest;
import com.prgrms.rg.web.ridingpost.requests.RidingSaveMainRequest;
import com.prgrms.rg.web.ridingpost.requests.RidingPostSaveRequest;

@ControllerTest(controllers = RidingPostController.class)
@AutoConfigureRestDocs
class RidingPostControllerTest {

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
	@DisplayName("RidingPost 생성 controller")
	void createRidingTest() throws Exception {

		//given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var subRequest = new RidingSaveDetailRequest("sub-section", "~~~~wow", List.of(1L));

		var body = new RidingPostSaveRequest(
			new RidingSaveMainRequest("title", LocalDateTime.now().plusDays(10L),
				"2시간 30분", List.of("start", "end"), 10000, 5, 20,
				10001, new Coordinate(0, 0), "상", List.of("MTB", "로드"), null),
			List.of(subRequest));

		var bodyString = objectMapper.writeValueAsString(body);
		//when
		when(ridingPostService.createRidingPost(1L, body.toCommand())).thenReturn(2L);
		when(ridingPostService.createRidingPost(anyLong(), any())).thenReturn(2L);

		mockMvc.perform(post("/api/v1/ridingposts")
				.header("Authorization", "token " + token)
				.content(bodyString)
				.contentType("application/json")
			).andExpect(status().isOk())
			.andDo(document("ridingpost-create",
				requestHeaders(
					headerWithName("Authorization").description("유저를 인증할 토큰")
				),
				requestFields(
					fieldWithPath("information.title").description("제목"),
					fieldWithPath("information.ridingDate").description("일시"),
					fieldWithPath("information.estimatedTime").description("예상시간"),
					fieldWithPath("information.routes").description("이동 경로"),
					fieldWithPath("information.fee").description("회비"),
					fieldWithPath("information.minParticipantCount").description("최소 참여자 수"),
					fieldWithPath("information.maxParticipantCount").description("최대 참여자 수"),
					fieldWithPath("information.regionCode").description("지역 코드"),
					fieldWithPath("information.departurePlace").description("출발 위치"),
					fieldWithPath("information.departurePlace.lng").description("경도"),
					fieldWithPath("information.departurePlace.lat").description("위도"),
					fieldWithPath("information.level").description("난이도"),
					fieldWithPath("information.bicycleTypes").description("자전거 종류 리스트"),
					fieldWithPath("information.thumbnail").description("썸네일 이미지 ID"),

					fieldWithPath("details[].title").description("Sub-section 제목"),
					fieldWithPath("details[].content").description("Sub-section 내용"),
					fieldWithPath("details[].images[]").description("Sub-section 이미지 ID 리스트")
				),
				responseBody()
			))
			.andDo(print());

	}

	@Test
	@DisplayName("ridingpost controller 생성 실패 테스트 - 잘못된 입력")
	void handleIllegalExceptionTest() throws Exception {

		//given
		var token = tokenProvider.createToken("ROLE_USER", 1L);

		var body = new RidingPostSaveRequest(
			new RidingSaveMainRequest("error-title", LocalDateTime.now().plusDays(10L),
				"12321???", List.of("start", "end"), 10000, 5, 20,
				10001, new Coordinate(0, 0), "모름", List.of("MTB", "로드"), null), Collections.emptyList());

		var bodyString = objectMapper.writeValueAsString(body);

		//when
		when(ridingPostService.createRidingPost(anyLong(), any(RidingSaveCommand.class))).thenThrow(
			new IllegalArgumentException());

		//then
		mockMvc.perform(post("/api/v1/ridingposts")
				.header("Authorization", "token " + token)
				.content(bodyString)
				.contentType("application/json")
			).andExpect(status().isBadRequest())
			.andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
			.andDo(print());
	}

	@Test
	@DisplayName("ridingpost 수정 controller")
	void modifyRidingTest() throws Exception {

		//given
		Long leaderId = 1L;
		Long postId = 10L;
		var token = tokenProvider.createToken("ROLE_USER", leaderId);

		var subRequest = new RidingSaveDetailRequest("sub-section", "~~~~wow", List.of(1L));
		var body = new RidingPostSaveRequest(
			new RidingSaveMainRequest("title", LocalDateTime.now().plusDays(10L),
				"2시간 30분", List.of("start", "end"), 10000, 5, 20,
				10001, new Coordinate(0, 0), "상", List.of("MTB", "로드"), null), List.of(subRequest));

		var bodyString = objectMapper.writeValueAsString(body);

		when(ridingPostService.updateRidingPost(leaderId, postId, body.toCommand())).thenReturn(postId);
		when(ridingPostService.updateRidingPost(anyLong(), anyLong(), any())).thenReturn(postId);
		//when
		mockMvc.perform(put("/api/v1/ridingposts/{postId}", postId)
				.header("Authorization", "token " + token)
				.contentType("application/json")
				.content(bodyString))
			.andExpect(status().isOk())
			.andDo(document("ridingpost-update",
				requestHeaders(
					headerWithName("Authorization").description("유저를 인증할 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("수정할 게시글 ID")
				),
				requestFields(
					fieldWithPath("information.title").description("제목"),
					fieldWithPath("information.ridingDate").description("일시"),
					fieldWithPath("information.estimatedTime").description("예상시간"),
					fieldWithPath("information.routes").description("이동 경로"),
					fieldWithPath("information.fee").description("회비"),
					fieldWithPath("information.minParticipantCount").description("최소 참여자 수"),
					fieldWithPath("information.maxParticipantCount").description("최대 참여자 수"),
					fieldWithPath("information.regionCode").description("지역 코드"),
					fieldWithPath("information.departurePlace").description("출발 위치"),
					fieldWithPath("information.departurePlace.lng").description("경도"),
					fieldWithPath("information.departurePlace.lat").description("위도"),
					fieldWithPath("information.level").description("난이도"),
					fieldWithPath("information.bicycleTypes").description("자전거 종류 리스트"),
					fieldWithPath("information.thumbnail").description("썸네일 이미지 ID"),

					fieldWithPath("details[].title").description("Sub-section 제목"),
					fieldWithPath("details[].content").description("Sub-section 내용"),
					fieldWithPath("details[].images[]").description("Sub-section 이미지 ID 리스트")
				),
				responseBody()
			))
			.andDo(print());
	}

	@Test
	@DisplayName("ridingpost 삭제 controller")
	void deleteRidingTest() throws Exception {

		//given
		Long leaderId = 1L;
		Long postId = 10L;
		var token = tokenProvider.createToken("ROLE_USER", leaderId);

		doNothing().when(ridingPostService).deleteRidingPost(leaderId, postId);

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/api/v1/ridingposts/{postId}", postId)
					.header("Authorization", "token " + token)
					.contentType("application/json"))
			.andExpect(status().isOk())
			.andDo(document("ridingpost-delete",
				requestHeaders(
					headerWithName("Authorization").description("유저를 인증할 토큰")
				),
				pathParameters(
					parameterWithName("postId").description("삭제할 게시글 ID")
				),
				responseBody()
				))
			.andDo(print());

	}

	@Test
	@DisplayName("허가되지 않은 유저 요청 - UnAuthorizedException")
	void handleUnAuthorizedExceptionTest() throws Exception {

		Long leaderId = 1L;
		Long postId = 10L;
		var token = tokenProvider.createToken("ROLE_USER", leaderId);

		doThrow(new UnAuthorizedException(leaderId)).when(ridingPostService).deleteRidingPost(leaderId, postId);

		mockMvc.perform(delete("/api/v1/ridingposts/{postId}", postId)
				.header("Authorization", "token " + token)
				.contentType("application/json"))
			.andExpect(status().isUnauthorized())
			.andDo(print());

	}

}