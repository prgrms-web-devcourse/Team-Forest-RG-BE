package com.prgrms.rg.domain.ridingpost.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RidingPostSearchRepository {
	Slice<RidingPostInfo> searchRidingPostSlice(RidingSearchCondition condition, Pageable pageable);

}
