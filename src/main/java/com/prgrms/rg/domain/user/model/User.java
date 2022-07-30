package com.prgrms.rg.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.model.BaseTimeEntity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Embedded
	private Nickname nickname;

	@Embedded
	private RiderProfile profile;

	@OneToOne(mappedBy = "user")
	private ProfileImage profileImage;

	//TODO: 연락처 추가

	@Embedded
	private Introduction introduction;

	@Embedded
	private Manner manner;
}
