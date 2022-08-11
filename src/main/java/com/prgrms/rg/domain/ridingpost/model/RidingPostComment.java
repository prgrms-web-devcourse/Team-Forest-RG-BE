package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.*;
import static javax.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.logging.log4j.util.Strings;

import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@OneToMany(mappedBy = "parentComment")
	private List<RidingPostComment> childComments;

	@Column(length = 500, nullable = false)
	private String content;

	private RidingPostComment(User author, RidingPost ridingPost, String content) {
		checkNotNull(author);
		checkArgument(Strings.isNotBlank(content));
		this.author = author;
		this.ridingPost = ridingPost;
		this.content = content;
		this.childComments = new ArrayList<>();
	}

	public static RidingPostComment of(User author, RidingPost post, String content) {
		return new RidingPostComment(author, post, content);
	}

	public static RidingPostComment createChildPost(User author, RidingPostComment parentComment, String content) {
		var newPost = new RidingPostComment(author, null, content);
		newPost.assignToParent(parentComment);
		return newPost;
	}

	// TODO : 엔티티 분리
	public boolean isRootComment() {
		return Objects.nonNull(ridingPost);
	}

	private void assignToParent(RidingPostComment parentComment) {
		checkArgument(!isRootComment(), "RidingPost가 존재할 경우 부모 댓글에 할당할 수 없습니다.");
		checkNotNull(parentComment);
		parentComment.getChildComments().add(this);
		this.parentComment = parentComment;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RidingPostComment))
			return false;
		RidingPostComment comment = (RidingPostComment)o;
		return getId().equals(comment.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
