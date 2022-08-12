package com.prgrms.rg.web.user.api;

import static com.prgrms.rg.domain.common.model.metadata.RidingLevel.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingConditionSection;
import com.prgrms.rg.domain.ridingpost.model.RidingMainSection;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantSection;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.RiderProfile;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.information.ParticipatedRidingInfo;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;
import com.prgrms.rg.infrastructure.repository.projections.querydsl.RidingPostBriefInfoQueryDslProjection;
import com.prgrms.rg.testutil.ControllerTest;

@ControllerTest(controllers = UserReadRestControllerV1.class)
class UserReadRestControllerV1Test {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserReadService userReadService;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("User 프로필 읽기 테스트")
	void updateUserTest() throws Exception {
		//Given
		var token = tokenProvider.createToken("ROLE_USER", 1L);
		User user = User.builder()
			.id(1L)
			.nickname(new Nickname("outwater"))
			.introduction(
				new Introduction("자기소개입니다. 저는 라이딩을 사랑하는 라죽남 입니다. 로드 MTB 따릉이 가리지 않고, 자전거가 있는 모든 곳을 환영합니다. 같이 타고 즐겨보아요."))
			.manner(Manner.create())
			.profile(new RiderProfile(2012, RidingLevel.INTERMEDIATE))
			.phoneNumber("010-1234-5678")
			.email("kakaoemail@naver.com")
			.profileImage(null)
			.build();
		user.addBicycle(new Bicycle(1L, "로드"));
		user.addBicycle(new Bicycle(2L, "따릉이"));

		var riding = new RidingPostBriefInfoQueryDslProjection(1L, "한강 라이딩 하실분",
			"https://programmers.co.kr/assets/icons/apple-icon-6eafc2c4c58a21aef692d6e44ce99d41f999c71789f277317532d0a9c6db8976.png",
			INTERMEDIATE.name(), LocalDateTime.of(2022, 12, 22, 22, 22), new Coordinate(37.660666, 126.229333));

		UserProfilePageInfo result = UserProfilePageInfo.from(
			user, ParticipatedRidingInfo.from(List.of(riding), Collections.emptyList(), List.of(riding)));

		when(userReadService.getUserProfilePageInfo(1L)).thenReturn(result);

		//When
		mockMvc.perform(get(("/api/v1/users/1"))
				.contentType("application/json")
			).andExpect(status().isOk())
			.andDo(print());
	}
}