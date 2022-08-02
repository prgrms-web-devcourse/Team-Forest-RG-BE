package com.prgrms.rg.domain.ridingpost.model.dummy;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO 어플리케이션 로드 시 자전거 데이터 삽입

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bicycle {

	@Id
	private Long id;

	private String name;

}
