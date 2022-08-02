package com.prgrms.rg.domain.ridingpost.model;

import com.prgrms.rg.domain.ridingpost.model.dummy.RidingPost;
import com.prgrms.rg.domain.user.model.User;

public interface RidingParticipantRepository {
	boolean existsByUserAndPost(User user, RidingPost post);
}
