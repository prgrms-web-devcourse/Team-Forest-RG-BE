package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.*;
import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

	@ManyToOne(optional = false, fetch = LAZY)
	private RidingPost ridingPost;

	@Column(length = 500, nullable = false)
	private String content;

	private RidingPostComment(User author, RidingPost ridingPost, String content) {
		checkNotNull(author);
		checkNotNull(ridingPost);
		checkArgument(Strings.isNotBlank(content));
		this.author = author;
		this.ridingPost = ridingPost;
		this.content = content;
	}

	public static RidingPostComment of(User author, RidingPost post, String content) {
		return new RidingPostComment(author, post, content);
	}
}
