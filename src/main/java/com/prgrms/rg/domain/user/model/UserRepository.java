package com.prgrms.rg.domain.user.model;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findByProviderAndProviderId(String provider, String providerId);
	Optional<User> findById(Long id);
	User save(User user);
}
