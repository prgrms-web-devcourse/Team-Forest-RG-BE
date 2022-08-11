package com.prgrms.rg.domain.ridingpost.application.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingPostSearchRepository;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchConditionValidator;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingSearchFailException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RidingPostReadServiceImpl implements RidingPostReadService {
	private final RidingPostRepository ridingPostRepository;
	private final RidingPostSearchRepository searchRepository;
	private final RidingSearchConditionValidator searchConditionValidator;

	@Override
	public RidingPost loadRidingPostById(Long postId) {
		return ridingPostRepository.findById(postId)
			.orElseThrow(() -> new RidingPostNotFoundException(postId));
	}

	@Override
	public RidingPostInfo loadRidingPostInfoById(Long postId) {
		RidingPost post = loadRidingPostById(postId);
		return RidingPostInfo.from(post);
	}

	@Override
	public Slice<RidingPostInfo> loadFilteredRidingPostByCondition(RidingSearchCondition condition, Pageable pageable) {
		try {
			searchConditionValidator.validate(condition);
			return searchRepository.searchRidingPostSlice(condition, pageable);
		} catch (IllegalArgumentException e) {
			throw new RidingSearchFailException(e);
		}
	}
}
