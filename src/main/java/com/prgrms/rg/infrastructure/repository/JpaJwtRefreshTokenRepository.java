package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.auth.JwtRefreshToken;
import com.prgrms.rg.domain.auth.JwtRefreshTokenRepository;

public interface JpaJwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> , JwtRefreshTokenRepository {

}
