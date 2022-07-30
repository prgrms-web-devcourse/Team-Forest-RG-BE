package com.prgrms.rg.domain.user.model;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * User와 Bicycle 사이의 중간 테이블
 */
@Entity
public class UserBicycle {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "bicycle_id")
	private Bicycle bicycle;
}
