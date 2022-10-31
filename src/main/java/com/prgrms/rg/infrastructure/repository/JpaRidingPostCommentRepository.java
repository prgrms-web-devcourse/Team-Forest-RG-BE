package com.prgrms.rg.infrastructure.repository;

import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPostCommentRepository;
import com.prgrms.rg.infrastructure.repository.custom.JpaRidingPostCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRidingPostCommentRepository extends JpaRepository<RidingPostComment, Long>, RidingPostCommentRepository,
    JpaRidingPostCommentRepositoryCustom {

}
