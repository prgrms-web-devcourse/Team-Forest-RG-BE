package com.prgrms.rg.web.user.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.user.application.UserService;
import com.prgrms.rg.web.user.results.UserDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	/**
	 * 보호받는 엔드포인트 - ROLE_USER 또는 ROLE_ADMIN 권한 필요함
	 */
	@GetMapping(path = "/user/me")
	public UserDto me(@AuthenticationPrincipal JwtAuthentication authentication) {
		return userService.findUserById(authentication.userId)
			.map(user ->
				new UserDto(authentication.token, authentication.userId)
			)
			.orElseThrow(() -> new IllegalArgumentException("Could not found user for " + authentication.userId));
	}
}
