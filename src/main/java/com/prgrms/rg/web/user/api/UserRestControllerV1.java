package com.prgrms.rg.web.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.user.application.UserService;
import com.prgrms.rg.web.user.results.UserInfoResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestControllerV1 {

	private final UserService userService;

	@GetMapping("/api/v1/users/{userId}")
	public UserInfoResult getAUserInformation(@PathVariable(name = "userId") Long userId) {
		return UserInfoResult.of(userService.getUserInformation(userId));
	}
}
