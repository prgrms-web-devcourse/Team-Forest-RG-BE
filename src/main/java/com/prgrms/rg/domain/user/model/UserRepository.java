package com.prgrms.rg.domain.user.model;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findById(Long userId);
}
