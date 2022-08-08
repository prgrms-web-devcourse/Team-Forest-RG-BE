package com.prgrms.rg.web.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.user.application.UserCommandService;
import com.prgrms.rg.web.user.requests.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserCommandRestControllerV1 implements UserRestController {

	private final UserCommandService userService;

	@PutMapping("/api/v1/users/{userId}")
	public ResponseEntity<Long> editUser(@PathVariable(name = "userId") Long userId,
		@RequestBody UserUpdateRequest request) {
		return ResponseEntity.ok().body(userService.edit(request.from(userId)));
	}
}
