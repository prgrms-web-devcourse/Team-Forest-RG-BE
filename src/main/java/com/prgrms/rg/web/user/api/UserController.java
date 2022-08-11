package com.prgrms.rg.web.user.api;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.user.application.UserAuthenticationService;
import com.prgrms.rg.domain.user.application.command.UserRegisterCommand;
import com.prgrms.rg.web.user.requests.OAuthLoginRequest;
import com.prgrms.rg.web.user.requests.UserRegisterRequest;
import com.prgrms.rg.web.user.results.OAuthLoginResult;
import com.prgrms.rg.web.user.results.UserMeResult;
import com.prgrms.rg.web.user.results.UserRegisterResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserAuthenticationService userAuthenticationService;

	@ExceptionHandler
	private ResponseEntity<String> forbiddenException(JWTVerificationException exception) {
		log.info("Not Allowed User , error = {}", exception.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not Allowed");
	}

	@ExceptionHandler
	private ResponseEntity<String> forbiddenException(NullPointerException exception) {
		log.info("Not Allowed User , error = {}", exception.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not Allowed");
	}

	@PostMapping("api/v1/users/oauth/login")
	public ResponseEntity<OAuthLoginResult> loginOAuth(@RequestBody OAuthLoginRequest loginRequest) throws
		IOException,
		InterruptedException {
		return ResponseEntity.status(HttpStatus.OK).body(userAuthenticationService.joinOAuth(loginRequest.getAuthorizationCode()));
	}

	@GetMapping("api/v1/user/me")
	public ResponseEntity<UserMeResult> me(@AuthenticationPrincipal JwtAuthentication authentication) {
		UserMeResult result = userAuthenticationService.checkUserById(authentication.userId,
			authentication.token);
		return ResponseEntity.status(HttpStatus.OK).header("Authorization", result.getToken()).body(result);
	}

	@Secured("ROLE_USER")
	@PostMapping("api/v1/users/register")
	public ResponseEntity<UserRegisterResult> registerUser(@AuthenticationPrincipal JwtAuthentication authentication, @RequestBody UserRegisterRequest userRegisterRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(userAuthenticationService.updateUserByRegistration(UserRegisterCommand.of(userRegisterRequest, authentication.userId)));
	}
}
