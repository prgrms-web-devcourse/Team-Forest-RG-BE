package com.prgrms.rg.domain.user.model;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;

import lombok.NoArgsConstructor;

/**
 * User와 Bicycle 사이의 중간 테이블
 */
@Entity
@NoArgsConstructor(access = PROTECTED)
public class UserBicycle {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@ManyToOne(fetch = LAZY, cascade = ALL)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "bicycle_id")
	private Bicycle bicycle;

	public UserBicycle(User user, Bicycle bicycle) {
		this.user = user;
		this.bicycle = bicycle;
	}

	String getName() {
		return bicycle.getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserBicycle))
			return false;

		UserBicycle that = (UserBicycle)o;

		return bicycle.getName().equals(that.bicycle.getName());
	}

	@Override
	public int hashCode() {
		return bicycle.getName().hashCode();
	}

	@Override
	public String toString() {
		return bicycle.toString();
	}
}
