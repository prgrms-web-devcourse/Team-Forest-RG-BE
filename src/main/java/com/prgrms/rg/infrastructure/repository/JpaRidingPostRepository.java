package com.prgrms.rg.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;

public interface JpaRidingPostRepository extends JpaRepository<RidingPost, Long>, RidingPostRepository {
	@Override
	Optional<RidingPost> findById(Long aLong);
}
