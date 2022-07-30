package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Bicycle {

	//TODO: 코드에 따라 id 값 부여
	public Bicycle(Long id) {
		this.id = id;
	}

	@Id
	private Long id;
	private String name;
}
