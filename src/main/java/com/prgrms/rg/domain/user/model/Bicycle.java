package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Bicycle {

	//TODO: 코드에 따라 id 값 부여
	//TODO: 어느 도메인에 속한 것일까?

	public Bicycle(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	private Long id;
	@Getter
	@Column(unique = true, nullable = false)
	private String name;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Bicycle))
			return false;

		Bicycle bicycle = (Bicycle)o;

		return name.equals(bicycle.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return "Bicycle{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}
