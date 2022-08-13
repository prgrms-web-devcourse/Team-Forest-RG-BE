package com.prgrms.rg.web.ridingpost.api;

import static com.prgrms.rg.testutil.TestEntityDataFactory.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.common.RelatedEntityNotFoundException;
import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.information.RidingPostCommentInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.testutil.ControllerTest;

@ControllerTest(controllers = RidingPostCommentController.class)
class RidingPostCommentControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	JwtTokenProvider tokenProvider;

	@MockBean
	RidingPostCommentService ridingPostCommentService;

	@Test
	@DisplayName("댓글 작성 요청을 명령을 위임하고 성공 결과를 응답한다.")
	void handle_post_comment_request_successfully() throws Exception {

		// Given
		String contents = "content";
		Map<String, Object> bodyMap = Map.of("contents", contents);
		var body = objectMapper.writeValueAsString(bodyMap);
		var authorId = 1L;
		var postId = 2L;
		var token = tokenProvider.createToken("ROLE_USER", authorId);
		var command = RidingPostCommentCreateCommand.of(authorId, postId, null, contents);
		given(ridingPostCommentService.createComment(command)).willReturn(1L);

		// When
		var result = mockMvc.perform(
			post("/api/v1/ridingposts/" + postId + "/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "token " + token)
				.content(body)
		);

		// Then
		result.andExpectAll(
			status().isOk(),
			jsonPath("id").value(1L)
		);
		then(ridingPostCommentService).should(times(1)).createComment(command);

	}

	@Test
	@DisplayName("유효하지 않은 사용자 id 혹은 라이딩 게시물 id로 요청했을 경우 정보 실패 메시지가 담긴 응답을 보낸다.")
	void send_error_message_when_requested_with_invalid_related_entity_id() throws Exception {
		// Given
		String content = "content";
		Map<String, Object> bodyMap = Map.of("contents", content);
		var body = objectMapper.writeValueAsString(bodyMap);
		var authorId = 1L;
		var postId = 2L;
		var token = tokenProvider.createToken("ROLE_USER", authorId);
		var command = RidingPostCommentCreateCommand.of(authorId, postId, null, content);
		given(ridingPostCommentService.createComment(command)).willThrow(RelatedEntityNotFoundException.class);

		// When
		var result = mockMvc.perform(
			post("/api/v1/ridingposts/" + postId + "/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "token " + token)
				.content(body)
		);

		// Then

		result.andExpectAll(
			status().isUnprocessableEntity(),
			jsonPath("message").value("필요한 사용자 정보, 라이딩 정보를 불러오는 데 실패했습니다.")
		);
		then(ridingPostCommentService).should(times(1)).createComment(command);
	}

	@Test
	@DisplayName("특정 RidingPost에 있는 댓글들을 대댓글과 함께 담아서 응답한다.")
	void response_comments_query_by_riding_post_id() throws Exception {

		// Given
		var author = createUser(1L);
		var commentAuthor = createUser(2L);
		var post = createRidingPost(author.getId());

		var rootComment = RidingPostComment.createRootComment(commentAuthor, post, "root");
		assignId(rootComment, 1L);
		var childComment = RidingPostComment.createChildComment(commentAuthor, rootComment, "child");
		assignId(childComment, 2L);

		var expectedReturn = List.of(RidingPostCommentInfo.from(rootComment));
		given(ridingPostCommentService.getCommentsByPostId(1L)).willReturn(expectedReturn);

		// When
		var result = mockMvc.perform(
			get("/api/v1/ridingposts/" + 1L + "/comments")
		);
		// Then
		result.andExpectAll(
			status().isOk(),
			content().json(objectMapper.writeValueAsString(Map.of("comments", expectedReturn)))
		);

	}

	@Test
	@DisplayName("댓글을 수정 요청을 성공적으로 수행하고, 수정한 댓글의 id를 반환한다.")
	void modify_comments_successfully() throws Exception {

		// Given
		var commentId = 1L;
		var userId = 2L;
		var postId = 3L;
		var requestMap = Map.of("contents", "updatedContents");
		var token = tokenProvider.createToken("ROLE_USER", userId);

		var body = objectMapper.writeValueAsString(requestMap);

		// When
		var result = mockMvc.perform(
			put("/api/v1/ridingposts/" + postId + "/comments/" + commentId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.header(HttpHeaders.AUTHORIZATION, "token " + token)
		);

		// Then
		then(ridingPostCommentService).should(times(1)).updateComment(userId, commentId, "updatedContents");
		result.andExpectAll(
			status().isOk(),
			jsonPath("id").value(commentId)
		);

	}

	@Test
	@DisplayName("댓글을 삭제 요청을 성공적으로 수행하고, 수정한 댓글의 id를 반환한다.")
	void remove_comments_successfully() throws Exception {

		// Given
		var commentId = 1L;
		var userId = 2L;
		var postId = 3L;
		var token = tokenProvider.createToken("ROLE_USER", userId);

		// When
		var result = mockMvc.perform(
			delete("/api/v1/ridingposts/" + postId + "/comments/" + commentId)
				.header(HttpHeaders.AUTHORIZATION, "token " + token)
		);

		// Then
		then(ridingPostCommentService).should(times(1)).removeComment(userId, commentId);
		result.andExpectAll(
			status().isOk(),
			jsonPath("id").value(commentId)
		);

	}

	private <T> void assignId(T target, long id) throws NoSuchFieldException, IllegalAccessException {
		Field idField = target.getClass().getDeclaredField("id");
		idField.setAccessible(true);
		idField.set(target, id);
		idField.setAccessible(false);
	}
}
