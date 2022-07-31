package com.prgrms.rg.domain.ridingpost.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//host 또한 여기에 추가되어야하는가??
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RidingParticipant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private RidingPost post;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	//host 고려 시 필요한 필드
	private boolean isHost;

	//해당 참여자가 평가를 진행하였는지 여부
	private boolean isEvaluated = false;

	public RidingParticipant(RidingPost post, User user, boolean isHost) {
		this.post = post;
		this.user = user;
		this.isHost = isHost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RidingParticipant that = (RidingParticipant)o;
		return post.equals(that.post) && user.equals(that.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(post, user);
	}
}
