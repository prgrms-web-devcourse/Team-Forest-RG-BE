package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.ridingpost.application.RidingJoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RidingJoinController {
	private final RidingJoinService ridingJoinService;

	@PostMapping("/api/v1/ridingposts/{postId}/join")
	ResponseEntity<String> join(@PathVariable Long postId,
		@AuthenticationPrincipal JwtToken token) {
		ridingJoinService.joinUserToRiding(token.getUserId(), postId);
		return ResponseEntity.ok("Riding join success");
	}
}
