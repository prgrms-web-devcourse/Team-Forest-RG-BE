package com.prgrms.rg.web.user.api;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.web.user.requests.OAuthLoginRequest;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserAuthenticationService userAuthenticationService;

	@PostMapping("api/v1/users/oauth/login")
	public OAuthLoginResult loginOAuth(@RequestBody OAuthLoginRequest loginRequest) throws
		IOException,
		InterruptedException {
		return userAuthenticationService.joinOAuth(loginRequest.getAuthorizationCode());
	}

	@GetMapping("api/v1/user/me")
	public UserMeResult me(@AuthenticationPrincipal JwtAuthentication authentication) {
		return userAuthenticationService.findUserById(authentication.userId);
	}
}
