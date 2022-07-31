package com.prgrms.rg.domain.user.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;

@Getter
@Entity
public class User {

	@Id
	private Long id;
}
