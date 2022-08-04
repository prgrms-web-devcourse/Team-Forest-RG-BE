package com.prgrms.rg.web.user.api;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	DataSource dataSource;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("요청 클라이언트 토큰의 유효성을 검증하고, 토큰에 할당된 사용자 ID를 제공한다.")
	void validate_token_and_display_granted_authorizations() throws Exception {

		// 임시 사용자 생성 및 토큰 주입
		var template = new JdbcTemplate(dataSource);
		template.update("INSERT INTO user(id,nickname) VALUES(3,'kms')");

		var token = tokenProvider.createToken("ROLE_USER", 3L);

		// Given
		var result = mockMvc.perform(
			MockMvcRequestBuilders.get("/user/me").header("Authorization", "token " + token));
		// When

		result.andDo(print())
			.andExpectAll(status().is2xxSuccessful(),
				jsonPath("token", instanceOf(String.class)),
				jsonPath("userId", equalTo(3))
			);

	}

}
