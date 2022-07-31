package com.prgrms.rg.domain.ridingpost.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 어플리케이션 로드 시 자전거 데이터 삽입
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bicycle {

	@Id
	private Long id;

	private String name;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Bicycle bicycle = (Bicycle)o;
		return name.equals(bicycle.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
