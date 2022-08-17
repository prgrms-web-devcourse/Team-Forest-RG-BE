package com.prgrms.rg.testutil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.ridingpost.model.exception.UnAuthorizedException;

/*
 * 테스트용 가짜 Controller
 * 반드시 예외를 발생시킵니다.
 */
@RestController
public class FakeRestController {
	@GetMapping("/fake")
	public void handleFakeRequest() {
		throw new RuntimeException("fake");
	}

	@GetMapping("/fake-unauthorized")
	public void handleFakeUnauthorizedRequest() {
		throw new UnAuthorizedException(5L);
	}
}
