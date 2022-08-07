package com.prgrms.rg.domain.common.model.metadata;

import static lombok.AccessLevel.*;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Setter(value = PRIVATE)
public class Bicycle {

	//TODO: 코드에 따라 id 값 부여
	//TODO: 어느 도메인에 속한 것일까?
	private static final Map<String, Bicycle> bicycleList = Map.of(
		"MTV", new Bicycle(1L, "MTV"),
		"로드", new Bicycle(2L, "로드"),
		"따릉이", new Bicycle(3L, "따릉이")
	);

	public Bicycle(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Bicycle from(String name) {
		//return check
		return bicycleList.get(name);
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
