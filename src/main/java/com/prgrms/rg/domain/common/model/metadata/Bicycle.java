package com.prgrms.rg.domain.common.model.metadata;

import static lombok.AccessLevel.*;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@Cacheable
@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Setter(value = PRIVATE)
public class Bicycle {

	//TODO: 코드에 따라 id 값 부여
	//TODO: 어느 도메인에 속한 것일까?

	public Bicycle(String name) {
		this.name = name;
	}

	public Bicycle(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	@Id
	private Long id;
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

	@UtilityClass
	public static class BicycleName {
		public static final String ALL = "상관없음";
		public static final String MTB = "MTB";
		public static final String ROAD = "로드";
		public static final String DDARUNGE = "따릉이";
		public static final String FIXIE = "픽시";
		public static final String HYBRID = "하이브리드";
	}

	@UtilityClass
	public static class BicycleCode {
		public static final Long ALL = 0L;
		public static final Long MTB = 1L;
		public static final Long ROAD = 2L;
		public static final Long DDARUNGE = 3L;
		public static final Long FIXIE = 4L;
		public static final Long HYBRID = 5L;
	}
}
