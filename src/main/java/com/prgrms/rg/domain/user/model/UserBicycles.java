package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;

/**
 * 일급 컬렉션 역할을 하는 Embedded Class입니다.
 * User <-> Bicycle 사이를 연결하는 UserBicycle엔티티에 접근합니다.
 * User(1): UserBicycle(N)
 */
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class UserBicycles {

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserBicycle> bicycles = new ArrayList<>();
}
