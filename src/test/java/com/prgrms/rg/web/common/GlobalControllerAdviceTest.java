package com.prgrms.rg.web.common;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.prgrms.rg.testutil.ControllerTest;
import com.prgrms.rg.testutil.FakeRestController;
import com.prgrms.rg.web.common.message.ExceptionMessageSender;

@ControllerTest(controllers = {FakeRestController.class})
class GlobalControllerAdviceTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ExceptionMessageSender exceptionMessageSender;

	@Test
	@DisplayName("다른 ControllerAdvice에서 처리하지 못한 예외들을 개발자에게 전송하고, 클라이언트에게 500 InternalServerError 메시지를 응답한다.")
	void handle_exception_send_response_with_internal_server_error() throws Exception {

		// Given
		// When
		var result = mockMvc.perform(MockMvcRequestBuilders.get("/fake"));

		// Then
		result.andExpectAll(
			status().is5xxServerError(),
			MockMvcResultMatchers.jsonPath("message").value(Matchers.equalTo("Internal Server Error"))
		);

		then(exceptionMessageSender).should(times(1)).send(any(Exception.class));

	}

}
