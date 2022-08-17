package com.prgrms.rg.domain.ridingpost.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;

public interface RidingPostReadService {
	RidingPost loadRidingPostById(Long postId);

	RidingPostInfo loadRidingPostInfoById(Long postId);

	Slice<RidingPostInfo> loadFilteredRidingPostByCondition(RidingSearchCondition condition, Pageable pageable);
}
