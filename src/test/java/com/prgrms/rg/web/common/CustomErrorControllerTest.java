package com.prgrms.rg.web.common;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.*;

import java.util.Map;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 기본 에러 처리 컨트롤러를 테스트 하려면 MockMvc가 아닌 실제 ServletContainer를 올려야 합니다.
 * 로컬에서 걸리는 테스트 시간을 고려하여 linux 환경(Github Action)에서만 테스트가 수행되도록 설정했습니다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomErrorControllerTest {

	@LocalServerPort
	int portNumber;
	@Autowired
	ObjectMapper json;

	@BeforeAll
	static void ignoreTestIfSystemIsNotLinuxEnvironment() {
		assumeThat(System.getProperty("os.name")).containsIgnoringCase("linux");
	}

	@Test
	@DisplayName("서버가 등록하지 않은 API 요청을 할 경우 404 메시지가 담긴 Json을 응답한다.")
	void send_404_status_code_response_with_json() throws Exception {

		// Given
		var restTemplate = new RestTemplate();
		var expectedMessage = json.writeValueAsString(Map.of("message", "Not Found"));
		var unregisteredUrl = "http://localhost:" + portNumber + "/unregsitered/url";

		// When
		ThrowableAssert.ThrowingCallable request = () -> restTemplate.getForObject(unregisteredUrl, Object.class);

		// Then
		assertThatExceptionOfType(HttpClientErrorException.class)
			.isThrownBy(request)
			.satisfies(
				exception -> assertThat(exception.getRawStatusCode()).isEqualTo(404),
				exception -> assertThat(exception.getResponseBodyAsString()).isEqualTo(expectedMessage)
			);
	}
}
