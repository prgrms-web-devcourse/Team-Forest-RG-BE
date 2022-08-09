package com.prgrms.rg.domain.ridingpost.application;

import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;

public interface RidingPostService {

	Long createRidingPost(Long userId, RidingSaveCommand command);

	Long updateRidingPost(Long leaderId, Long postId, RidingSaveCommand command);

}
