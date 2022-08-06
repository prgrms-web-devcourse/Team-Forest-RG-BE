package com.prgrms.rg.domain.ridingpost.application;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;

public interface RidingPostReadService {
	RidingPost getRidingPostEntityById(Long postId);
}
