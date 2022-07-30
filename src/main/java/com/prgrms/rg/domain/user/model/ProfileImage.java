package com.prgrms.rg.domain.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

//TODO: 머지 이후, 다음 PR에서 ImageEntity를 상속하는 형태로 변경
@Entity
public class ProfileImage {
	@Id
	@GeneratedValue
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
