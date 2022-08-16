// package com.prgrms.rg.web.user.api;
//
// import static com.prgrms.rg.domain.common.model.metadata.RidingLevel.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.restdocs.headers.HeaderDocumentation.*;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.restdocs.request.RequestDocumentation.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.time.LocalDateTime;
// import java.util.Collections;
// import java.util.List;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
// import org.springframework.test.web.servlet.MockMvc;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.prgrms.rg.domain.auth.jwt.JwtTokenProvider;
// import com.prgrms.rg.domain.common.model.metadata.Bicycle;
// import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
// import com.prgrms.rg.domain.ridingpost.application.information.RidingPostBriefInfo;
// import com.prgrms.rg.domain.ridingpost.model.Coordinate;
// import com.prgrms.rg.domain.user.application.UserReadService;
// import com.prgrms.rg.domain.user.model.Introduction;
// import com.prgrms.rg.domain.user.model.Manner;
// import com.prgrms.rg.domain.user.model.Nickname;
// import com.prgrms.rg.domain.user.model.RiderProfile;
// import com.prgrms.rg.domain.user.model.User;
// import com.prgrms.rg.domain.user.model.information.ParticipatedRidingInfo;
// import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;
// import com.prgrms.rg.testutil.ControllerTest;
//
// @AutoConfigureRestDocs
// @ControllerTest(controllers = UserReadRestControllerV1.class)
// class UserReadRestControllerV1Test {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@MockBean
// 	UserReadService userReadService;
//
// 	@Autowired
// 	JwtTokenProvider tokenProvider;
//
// 	@Autowired
// 	ObjectMapper objectMapper;
//
// 	@Test
// 	@DisplayName("User 프로필 읽기 테스트")
// 	void updateUserTest() throws Exception {
// 		//Given
// 		var token = tokenProvider.createToken("ROLE_USER", 1L);
// 		User user = User.builder()
// 			.id(1L)
// 			.nickname(new Nickname("outwater"))
// 			.introduction(
// 				new Introduction("자기소개입니다. 저는 라이딩을 사랑하는 라죽남 입니다. 로드 MTB 따릉이 가리지 않고, 자전거가 있는 모든 곳을 환영합니다. 같이 타고 즐겨보아요."))
// 			.manner(Manner.create())
// 			.profile(new RiderProfile(2012, RidingLevel.INTERMEDIATE))
// 			.phoneNumber("010-1234-5678")
// 			.email("kakaoemail@naver.com")
// 			.build();
// 		user.addBicycle(new Bicycle(1L, "로드"));
// 		user.addBicycle(new Bicycle(2L, "따릉이"));
//
// 		var riding = new RidingPostBriefInfo(1L, "한강 라이딩 하실분",
// 			"https://programmers.co.kr/assets/icons/apple-icon-6eafc2c4c58a21aef692d6e44ce99d41f999c71789f277317532d0a9c6db8976.png",
// 			INTERMEDIATE.name(), LocalDateTime.of(2022, 12, 22, 22, 22), new Coordinate(37.660666, 126.229333),
// 			new RidingPostBriefInfo.ZoneInfo(5, "서울시"),
// 			Collections.emptyList(), "1시간 30분", List.of("출발점", "도착점"));
//
// 		UserProfilePageInfo result = UserProfilePageInfo.from(
// 			user, ParticipatedRidingInfo.from(List.of(riding), Collections.emptyList(), List.of(riding),
// 				Collections.emptyList()));
//
// 		when(userReadService.getUserProfilePageInfo(1L)).thenReturn(result);
//
// 		//When
// 		mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/users/{userId}", 1L)
// 				.contentType("application/json")
// 				.header("Authorization", "token " +token)
// 			).andExpect(status().isOk())
// 			.andDo(document("user-get",
// 				requestHeaders(
// 					headerWithName("Authorization").description("유저를 인증할 토큰")
// 				),
// 				pathParameters(
// 					parameterWithName("userId").description("해당 유저 ID")
// 				),
// 				responseFields(
// 					fieldWithPath("privacyProfile.phoneNumber").description("유저 전화번호"),
// 					fieldWithPath("privacyProfile.kakaoEmail").description("유저 카카오 이메일"),
//
// 					fieldWithPath("ridingProfile.nickname").description("유저 닉네임"),
// 					fieldWithPath("ridingProfile.profileImage").description("유저 프로필 이미지 url"),
// 					fieldWithPath("ridingProfile.introduction").description("유저 자기소개"),
// 					fieldWithPath("ridingProfile.ridingStartYear").description("유저의 라이딩 시작년도"),
// 					fieldWithPath("ridingProfile.level").description("유저 레벨"),
// 					fieldWithPath("ridingProfile.favoriteRegionCode").description("유저의 지역번호"),
// 					fieldWithPath("ridingProfile.bicycles[]").description("유저의 자전거 리스트"),
//
// 					fieldWithPath("manner.mannerPoint").description("유저의 매너 점수"),
// 					fieldWithPath("manner.noShow").description("유저의 노쇼 카운트"),
// 					fieldWithPath("manner.banned").description("유저의 밴 날짜기한"),
//
// 					fieldWithPath("ridings.leading.id").description(""),
// 					fieldWithPath("ridings.leading").description(""),
// 					fieldWithPath("ridings.leading").description(""),
// 					fieldWithPath("ridings.leading").description(""),
// 					fieldWithPath("ridings.leading").description(""),
// 					fieldWithPath("ridings.leading").description(""),
// 				)
// 			))
// 			.andDo(print());
// 	}
// }