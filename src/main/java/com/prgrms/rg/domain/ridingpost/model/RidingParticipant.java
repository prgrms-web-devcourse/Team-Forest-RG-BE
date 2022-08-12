package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.*;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RidingParticipant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private RidingPost post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	//해당 참여자가 평가를 진행하였는지 여부
	private boolean isEvaluated = false;

	public RidingParticipant(RidingPost post, User user) {
		this.post = post;
		this.user = user;
	}

	public void setEvaluated() {
		isEvaluated = true;
	}

	public void checkEvaluated() {
		//todo exception make
		checkArgument(!isEvaluated);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RidingParticipant))
			return false;
		RidingParticipant that = (RidingParticipant)o;
		return post.equals(that.post) && user.equals(that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(post, user);
	}
}
