package com.prgrms.rg.web.ridingpost.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RidingInquiryController {
	private final RidingPostReadService postReadService;

	@GetMapping("/api/v1/ridingposts/{postId}")
	public ResponseEntity<RidingPostInfo> getRidingPostDetail(@PathVariable Long postId) {
		RidingPostInfo ridingPostInfo = postReadService.getRidingPostInfoById(postId);
		return ResponseEntity.ok(ridingPostInfo);
	}

}
