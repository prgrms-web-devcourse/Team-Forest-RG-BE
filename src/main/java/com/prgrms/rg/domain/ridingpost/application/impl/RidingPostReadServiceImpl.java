package com.prgrms.rg.domain.ridingpost.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostNotFoundException;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RidingPostReadServiceImpl implements RidingPostReadService {
	private final RidingPostRepository ridingPostRepository;

	@Override
	public RidingPost getRidingPostEntityById(Long postId) {
		return ridingPostRepository.findById(postId)
			.orElseThrow(() -> new RidingPostNotFoundException(postId));
	}
}
