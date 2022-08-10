package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.web.ridingpost.requests.RidingPostSaveRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RidingPostSaveController {

	private final RidingPostService ridingPostService;

	@Secured("ROLE_USER")
	@PostMapping(value = "/api/v1/ridingposts")
	public ResponseEntity<Long> registerRidingPost(@AuthenticationPrincipal JwtAuthentication auth,
		@RequestBody RidingPostSaveRequest ridingRequest) {

		//return type : post id
		return ResponseEntity.ok(ridingPostService.createRidingPost(auth.userId, ridingRequest.toCommand()));
	}

	@Secured("ROLE_USER")
	@PutMapping(value = "/api/v1/ridingposts/{postid}")
	public ResponseEntity<Long> modifyRidingPost(@AuthenticationPrincipal JwtAuthentication auth,
		@PathVariable(name = "postid") Long postId, @RequestBody RidingPostSaveRequest ridingRequest) {

		return ResponseEntity.ok(ridingPostService.updateRidingPost(auth.userId, postId, ridingRequest.toCommand()));
	}
}