package com.prgrms.rg.domain.user.model.dto;

import javax.validation.constraints.Pattern;

import com.prgrms.rg.domain.ridingpost.model.AddressCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRegisterDTO {
	private int ridingStartYear;
	private AddressCode favoriteRegionCode;
	private String level;
	private String phoneNumber;
	private String nickName;

	private UserRegisterDTO(Builder builder) {
		this.ridingStartYear = builder.ridingStartYear;
		this.favoriteRegionCode = builder.favoriteRegionCode;
		this.level = builder.level;
		this.phoneNumber = builder.phoneNumber;
		this.nickName = builder.nickName;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private int ridingStartYear;
		private AddressCode favoriteRegionCode;
		private String level;
		@Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "전화번호는 01x-xxx(x)-9999 형식입니다.")
		private String phoneNumber;
		private String nickName;

		private Builder() {
		}

		public Builder ridingStartYearAndPhoneNumber(int ridingStartYear, String phoneNumber) {
			this.ridingStartYear = ridingStartYear;
			this.phoneNumber = phoneNumber;
			return this;
		}

		public Builder nickNameAndLevel(String nickName, String level) {
			this.nickName = nickName;
			this.level = level;
			return this;
		}

		public Builder favoriteRegionCode(AddressCode favoriteRegionCode) {
			this.favoriteRegionCode = favoriteRegionCode;
			return this;
		}

		public UserRegisterDTO build() {
			return new UserRegisterDTO(this);
		}
	}
}
