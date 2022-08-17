package com.prgrms.rg.web.user.requests;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterRequest {
	private int ridingStartYear;
	private int favoriteRegionCode;
	List<String> bicycles;
	private String level;
	private String phoneNumber;
	private String nickname;

	public UserRegisterRequest(int ridingStartYear, int favoriteRegionCode, List<String> bicycles,
		String level, String phoneNumber, String nickName) {
		this.ridingStartYear = ridingStartYear;
		this.favoriteRegionCode = favoriteRegionCode;
		this.bicycles = bicycles;
		this.level = level;
		this.phoneNumber = phoneNumber;
		this.nickname = nickName;
	}
}
