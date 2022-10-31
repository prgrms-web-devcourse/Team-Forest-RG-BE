package com.prgrms.rg.infrastructure.repository.custom;

import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import java.util.List;

public interface JpaRidingPostCommentRepositoryCustom {

  List<RidingPostComment> findRootComments(RidingPost ridingPost);
}
