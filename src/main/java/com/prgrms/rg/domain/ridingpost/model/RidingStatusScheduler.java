package com.prgrms.rg.domain.ridingpost.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RidingStatusScheduler {

	private final RidingPostSearchRepository searchRepository;
	private final RidingPostRepository ridingPostRepository;

	/**
	 *  ridingDate check -> status update
	 */
	@Scheduled(cron = "0 0 0/6 * * *")
	public void updateRidingStatus() {
		log.info("updateRidingStatus - scheduler ");
		List<RidingPost> ridingList = searchRepository.searchRidingPostInProgress();

		List<RidingPost> updateList = new ArrayList<>();
		for (RidingPost post : ridingList) {
			if (post.checkneededStatus())
				updateList.add(post);
		}
		ridingPostRepository.updateRidingPostListToClosed(updateList);
	}
}
