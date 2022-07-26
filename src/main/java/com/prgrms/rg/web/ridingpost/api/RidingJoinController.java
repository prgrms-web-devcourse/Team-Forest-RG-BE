package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.ridingpost.application.impl.RidingJoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RidingJoinController {
	private final RidingJoinService ridingJoinService;

	@Secured("ROLE_USER")
	@PostMapping("/api/v1/ridingposts/{postId}/join")
	ResponseEntity<String> joinRiding(@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication auth) {
		ridingJoinService.joinUserToRiding(auth.userId, postId);
		return ResponseEntity.ok("Riding join success");
	}

	@Secured("ROLE_USER")
	@PostMapping("/api/v1/ridingposts/{postId}/cancel")
	ResponseEntity<String> cancelJoin(@PathVariable Long postId,
		@AuthenticationPrincipal JwtAuthentication auth) {
		ridingJoinService.cancelJoin(auth.userId, postId);
		return ResponseEntity.ok("canceling join successful");
	}
}
