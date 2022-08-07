package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.auth.jwt.JwtAuthentication;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.web.ridingpost.requests.RidingPostCreateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController(value = "/api/v1")
public class RestRidingPostCreateController {

	private final RidingPostService ridingPostService;

	@PostMapping(value = "/ridingposts")
	public ResponseEntity<Long> createRidingPost(@AuthenticationPrincipal JwtAuthentication auth,
		@RequestBody RidingPostCreateRequest ridingRequest) {

		//return type : post id
		return ResponseEntity.ok(ridingPostService.createRidingPost(auth.userId, ridingRequest.toCommand()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handlerIllegalArgument(IllegalArgumentException e) {

		log.warn("ILLEGAL ARGUMENT", e);
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
