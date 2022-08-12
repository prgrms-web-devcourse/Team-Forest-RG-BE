package com.prgrms.rg.domain.auth;

import java.util.Optional;

public interface JwtRefreshTokenRepository {
	JwtRefreshToken save(JwtRefreshToken jwtRefreshToken);

	Optional<JwtRefreshToken> findByUserId(Long userId);

	Optional<JwtRefreshToken> findById(Long id);

	void delete(JwtRefreshToken jwtRefreshToken);
}
