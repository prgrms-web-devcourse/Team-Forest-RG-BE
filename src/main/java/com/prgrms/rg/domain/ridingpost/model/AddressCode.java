package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//로드 시 테이블에 데이터 삽입
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AddressCode {

	@Id
	private int code;

	//도
	@Column(name = "area")
	private String area;

	//시
	@Column(name = "city")
	private String city;

	//군, 구
	@Column(name = "district")
	private String district;

	public AddressCode(int code) {
		this.code = code;
	}

}
