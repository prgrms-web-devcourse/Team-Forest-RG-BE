package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static javax.persistence.FetchType.LAZY;

import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.user.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.BatchSize;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RidingPostComment extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = LAZY)
  private User author;

  @ManyToOne(fetch = LAZY)
  private RidingPost ridingPost;

  @ManyToOne(fetch = LAZY)
  private RidingPostComment parentComment;
  @BatchSize(size = 100)
  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE)
  private List<RidingPostComment> childComments;

  @Column(length = 500, nullable = false)
  private String contents;

  private RidingPostComment(User author, RidingPost ridingPost, String contents) {
    checkNotNull(author);
    checkArgument(Strings.isNotBlank(contents));
    this.author = author;
    this.ridingPost = ridingPost;
    this.contents = contents;
    this.childComments = new ArrayList<>();
  }

  public static RidingPostComment createRootComment(User author, RidingPost post, String content) {
    checkNotNull(post);
    return new RidingPostComment(author, post, content);
  }

  public static RidingPostComment createChildComment(User author, RidingPostComment parentComment, String content) {
    var newPost = new RidingPostComment(author, null, content);
    newPost.assignToParent(parentComment);
    return newPost;
  }

  public void changeContents(String contents) {
    checkArgument(Strings.isNotBlank(contents));
    this.contents = contents;
  }

  public boolean isChildComment() {
    return Objects.nonNull(parentComment);
  }

  private void assignToParent(RidingPostComment parentComment) {
    checkArgument(!isChildComment(), "이미 부모 댓글이 존재합니다.");
    checkNotNull(parentComment);
    parentComment.getChildComments().add(this);
    this.parentComment = parentComment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RidingPostComment)) {
      return false;
    }
    RidingPostComment comment = (RidingPostComment) o;
    return getId().equals(comment.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
