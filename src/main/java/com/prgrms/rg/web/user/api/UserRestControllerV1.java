package com.prgrms.rg.web.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.web.user.results.UserProfileResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestControllerV1 implements UserRestController {

	private final UserReadService userService;

	@GetMapping("/api/v1/users/{userId}")
	public UserProfileResult getUserInformation(@PathVariable(name = "userId") Long userId) {
		return UserProfileResult.of(userService.getUserProfilePageInfo(userId));
	}
}
