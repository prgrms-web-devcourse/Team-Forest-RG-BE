package com.prgrms.rg.web.ridingpost.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RidingPostInquiryController {
	private final RidingPostReadService postReadService;

	@GetMapping("/api/v1/ridingposts/{postId}")
	public ResponseEntity<RidingPostInfo> getRidingPostDetail(@PathVariable Long postId) {
		RidingPostInfo ridingPostInfo = postReadService.loadRidingPostInfoById(postId);
		return ResponseEntity.ok(ridingPostInfo);
	}

	@GetMapping("api/v1/ridingposts")
	public ResponseEntity<Slice<RidingPostInfo>> getRidingPostList(
		@ModelAttribute RidingSearchCondition searchCondition,
		@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable) {
		log.info(searchCondition.toString());
		log.info(pageable.toString());
		var ridingPostSlice = postReadService.loadFilteredRidingPostByCondition(searchCondition, pageable);
		return ResponseEntity.ok(ridingPostSlice);
	}

}
