package com.prgrms.rg.web.ridingpost.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.web.ridingpost.requests.RidingPostCreateRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController(value = "/api/v1")
public class RestRidingPostCreateController {

	private final RidingPostService ridingPostService;

	@PostMapping(value = "/ridingposts")
	public Long createRidingPost(@AuthenticationPrincipal Long userId, @RequestBody RidingPostCreateRequest ridingRequest) {

		//return type : post id
		return ridingPostService.createRidingPost(userId, ridingRequest.toCommand());
	}
}
