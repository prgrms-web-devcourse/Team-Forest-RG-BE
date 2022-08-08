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
	private Integer code;

	//시, 도
	@Column(name = "area")
	private String area;

	//군, 구
	@Column(name = "district")
	private String district;

	public AddressCode(Integer code) {
		this.code = code;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AddressCode))
			return false;

		AddressCode that = (AddressCode)o;

		return code.equals(that.code);
	}

	@Override
	public int hashCode() {
		return code.hashCode();
	}
}
