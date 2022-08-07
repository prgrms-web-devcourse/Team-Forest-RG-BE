package com.prgrms.rg.domain.ridingpost.application;

import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;

public interface RidingPostService {

	Long createRidingPost(Long userId, RidingCreateCommand command);
}
