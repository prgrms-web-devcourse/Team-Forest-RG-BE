package com.prgrms.rg.domain.user.application;

import com.prgrms.rg.domain.user.model.User;

public interface UserService {
	User getUserInformation(Long userId);
}
