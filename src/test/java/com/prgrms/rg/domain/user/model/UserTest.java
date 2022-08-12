package com.prgrms.rg.domain.user.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;


import com.prgrms.rg.domain.common.model.metadata.RidingLevel;

import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.user.model.dto.UserRegisterDTO;

class UserTest {

	@Test
	void updateByRegistration_정상_테스트() {
		//given
		int ridingStartYear = 1996;
		Integer favoriteRegionCode = 23;
		String level = "하";
		String phoneNumber = "010-5687-1234";
		String nickName = "hunkiKim";
		UserRegisterDTO dto = UserRegisterDTO.builder()
			.favoriteRegionCode(new AddressCode(favoriteRegionCode))
			.ridingStartYearAndPhoneNumber(ridingStartYear, phoneNumber)
			.nickNameAndLevel(nickName, level)
			.build();
		// Bicycle mtb = bicycleRepository.save(new Bicycle(999L,"MTB"));
		User user = User.builder()
			.nickname(new Nickname("adminNickname"))
			.manner(Manner.create())
			.isRegistered(true)
			.introduction(new Introduction("관리자입니다."))
			.provider("kakao")
			.providerId("provider_id")
			.profile(new RiderProfile(1996, RidingLevel.BEGINNER))
			.addressCode(new AddressCode(1))
			.build();
		//when
		user.updateByRegistration(dto);
		// then
		assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
		assertThat(user.getAddressCode().getCode()).isEqualTo(favoriteRegionCode);
		assertThat(user.getNickname()).isEqualTo(nickName);
		assertThat(user.getRiderInformation().getRidingYears()).isEqualTo(ridingStartYear);
	}

	@Test
	void phoneNumber_비정상_테스트() {
		//given
		int ridingStartYear = 1996;
		Integer favoriteRegionCode = 23;
		String level = "하";
		String noSnakePhoneNumber = "01056789492";
		String wrongPhoneNumber = "123-5687-5643";
		String nickName = "hunkiKim";
		UserRegisterDTO dto = UserRegisterDTO.builder()
			.favoriteRegionCode(new AddressCode(favoriteRegionCode))
			.ridingStartYearAndPhoneNumber(ridingStartYear, noSnakePhoneNumber)
			.nickNameAndLevel(nickName, level)
			.build();
		UserRegisterDTO dto2 = UserRegisterDTO.builder()
			.favoriteRegionCode(new AddressCode(favoriteRegionCode))
			.ridingStartYearAndPhoneNumber(ridingStartYear, wrongPhoneNumber)
			.nickNameAndLevel(nickName, level)
			.build();
		User user = User.builder()
			.nickname(new Nickname("adminNickname"))
			.manner(Manner.create())
			.isRegistered(true)
			.introduction(new Introduction("관리자입니다."))
			.provider("kakao")
			.providerId("provider_id")
			.profile(new RiderProfile(1996, RidingLevel.BEGINNER))
			.addressCode(new AddressCode(1))
			.build();
		//when // then
		assertThatThrownBy(() -> {
			user.updateByRegistration(dto);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 번호입니다.");
		assertThatThrownBy(() -> {
			user.updateByRegistration(dto2);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("잘못된 번호입니다.");
	}
}
