package com.prgrms.rg.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
	@Query("select u from User u where u.provider = :provider and u.providerId = :providerId")
	Optional<User> findByProviderAndProviderId(@Param("provider") String provider,
		@Param("providerId") String providerId);
}
