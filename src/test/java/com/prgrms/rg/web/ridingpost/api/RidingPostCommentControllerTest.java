package com.prgrms.rg.web.ridingpost.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.prgrms.rg.domain.ridingpost.application.RidingPostCommentService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingPostCommentCreateCommand;
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
		String content = "content";
		Map<String, Object> bodyMap = Map.of("content", content);
		var body = objectMapper.writeValueAsString(bodyMap);
		var authorId = 1L;
		var postId = 2L;
		var token = tokenProvider.createToken("ROLE_USER", authorId);
		var command = RidingPostCommentCreateCommand.of(authorId, postId, content);
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

}
