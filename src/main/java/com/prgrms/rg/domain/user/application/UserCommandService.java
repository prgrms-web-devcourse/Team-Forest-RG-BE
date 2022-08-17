package com.prgrms.rg.domain.user.application;

import com.prgrms.rg.domain.user.application.command.UserUpdateCommand;

public interface UserCommandService {
	Long edit(UserUpdateCommand command, Long userId);
}
