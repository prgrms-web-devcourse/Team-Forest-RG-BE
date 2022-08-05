package com.prgrms.rg.domain.user.application;

import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

public interface UserReadService {
	User getUserEntityById(Long userId);

	UserProfilePageInfo getUserProfilePageInfo(Long userId);
}
