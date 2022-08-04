package com.prgrms.rg.domain.user.application;

import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

public interface UserReadService {
	UserProfilePageInfo getUserProfilePageInfo(Long userId);
}
