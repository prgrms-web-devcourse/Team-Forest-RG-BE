package com.prgrms.rg.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.infrastructure.repository.custom.JpaRidingPostRepositoryCustom;

public interface JpaRidingPostRepository extends JpaRepository<RidingPost, Long>, RidingPostRepository,
	JpaRidingPostRepositoryCustom {
	@Override
	Optional<RidingPost> findById(Long aLong);
}
