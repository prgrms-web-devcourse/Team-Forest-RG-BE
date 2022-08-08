package com.prgrms.rg.domain.ridingpost.model;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.model.exception.RidingPostNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RidingPostFinder {
	private final RidingPostRepository ridingPostRepository;

	public RidingPost find(Long ridingPostId) {
		return ridingPostRepository.findById(ridingPostId)
			.orElseThrow(() -> new RidingPostNotFoundException(ridingPostId));
	}

}
