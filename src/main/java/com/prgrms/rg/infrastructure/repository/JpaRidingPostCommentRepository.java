package com.prgrms.rg.infrastructure.repository;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPostCommentRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRidingPostCommentRepository extends JpaRepository<RidingPostComment, Long>, RidingPostCommentRepository {

  List<RidingPostComment> findAllByRidingPostAndParentCommentIsNull(RidingPost ridingPost);
}
