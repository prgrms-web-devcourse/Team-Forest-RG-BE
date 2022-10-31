package com.prgrms.rg.infrastructure.repository;

import com.prgrms.rg.domain.ridingpost.model.QRidingPostComment;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostComment;
import com.prgrms.rg.infrastructure.repository.custom.JpaRidingPostCommentRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class JpaRidingPostCommentRepositoryImpl extends QuerydslRepositorySupport implements JpaRidingPostCommentRepositoryCustom {

  private static final QRidingPostComment comment = QRidingPostComment.ridingPostComment;

  public JpaRidingPostCommentRepositoryImpl() {
    super(RidingPostComment.class);
  }

  @Override
  public List<RidingPostComment> findRootComments(RidingPost ridingPost) {

    return from(comment)
        .where(comment.parentComment.isNull(), comment.ridingPost.eq(ridingPost))
        .join(comment.author).fetchJoin()
        .fetch();
  }
}
