package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPostCommentRepository;

public interface JpaRidingPostCommentRepository extends JpaRepository<RidingPostComment, Long>, RidingPostCommentRepository {
}
