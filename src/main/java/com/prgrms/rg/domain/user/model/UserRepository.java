package com.prgrms.rg.domain.user.model;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findByProviderAndProviderId(String provider, String providerId);

	User save(User user);

	Optional<User> findById(Long userId);
}
