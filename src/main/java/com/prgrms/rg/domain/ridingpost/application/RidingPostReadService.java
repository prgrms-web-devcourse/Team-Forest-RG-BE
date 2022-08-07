package com.prgrms.rg.domain.ridingpost.application;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;

public interface RidingPostReadService {
	RidingPost getRidingPostById(Long postId);

	RidingPostInfo getRidingPostInfoById(Long postId);
}
