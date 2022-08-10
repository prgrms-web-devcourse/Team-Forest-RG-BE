package com.prgrms.rg.web.user.requests;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterRequest {
	private int ridingStartYear;
	private int favoriteRegionCode;
	List<String> bicycles;
	private String level;
	private String phoneNumber;
	private String nickName;

	public UserRegisterRequest(int ridingStartYear, int favoriteRegionCode, List<String> bicycles,
		String level, String phoneNumber, String nickName) {
		this.ridingStartYear = ridingStartYear;
		this.favoriteRegionCode = favoriteRegionCode;
		this.bicycles = bicycles;
		this.level = level;
		this.phoneNumber = phoneNumber;
		this.nickName = nickName;
	}
}