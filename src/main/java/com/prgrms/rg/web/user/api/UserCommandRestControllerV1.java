package com.prgrms.rg.web.user.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.user.application.UserCommandService;
import com.prgrms.rg.web.user.requests.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserCommandRestControllerV1 implements UserRestController {

	private final UserCommandService userService;

	@Secured("ROLE_USER")
	@PutMapping("/api/v1/users/{userId}")
	public ResponseEntity<Long> editUser(@Valid @RequestBody UserUpdateRequest request,
		@AuthenticationPrincipal JwtAuthentication auth,
		@PathVariable(name = "userId") Long userId) {
		return ResponseEntity.ok().body(userService.edit(request.toCommand(userId), auth.userId));
	}
}
