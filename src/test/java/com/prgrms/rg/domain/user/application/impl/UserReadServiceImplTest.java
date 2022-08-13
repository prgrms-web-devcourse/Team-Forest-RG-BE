package com.prgrms.rg.domain.user.application.impl;

import static com.prgrms.rg.domain.common.model.metadata.RidingLevel.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingConditionSection;
import com.prgrms.rg.domain.ridingpost.model.RidingMainSection;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantSection;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.model.Introduction;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.RiderProfile;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.domain.user.model.information.UserProfilePageInfo;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
class UserReadServiceImplTest {

	@Autowired
	UserReadServiceImpl userReadService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RidingPostRepository ridingPostRepository;
	@Autowired
	BicycleRepository bicycleRepository;
	@PersistenceContext
	EntityManager em;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("유저 정보를 성공적으로 읽어올 수 있음")
	void getUserProfilePageInfo() throws JsonProcessingException {
		//Given
		User user = User.builder()
			.nickname(new Nickname("RG라이더"))
			.profile(new RiderProfile(2019, INTERMEDIATE))
			.manner(Manner.create())
			.introduction(new Introduction("잘 부탁드립니다. 자전거를 좋아하는 아들 둘 엄마입니다."))
			.phoneNumber("010-1234-5678")
			.build();
		user.addBicycle(bicycleRepository.findById(2L).get());
		user.addBicycle(bicycleRepository.findById(3L).get());
		userRepository.save(user);

		User otherUser = User.builder()
			.nickname(new Nickname("자전거매니아"))
			.profile(new RiderProfile(2022, MASTER))
			.manner(Manner.create())
			.introduction(new Introduction("자전거 너무 좋아요."))
			.phoneNumber("010-1234-5679")
			.build();
		otherUser.addBicycle(bicycleRepository.findById(4L).get());
		userRepository.save(otherUser);

		RidingPost ridingPost = RidingPost.builder()
			.leader(user)
			.ridingMainSection(new RidingMainSection("한강 라이딩 하실분", "4시간",
				LocalDateTime.of(2022, 12, 22, 22, 22), 2000,
				new AddressCode(11010), List.of("시작점", "도착지점"), new Coordinate(37.660666, 126.229333)))
			.ridingConditionSection(new RidingConditionSection(BEGINNER))
			.ridingParticipantSection(new RidingParticipantSection(0, 10))
			.build();

		ridingPost.addParticipant(user);

		RidingPost otherRidingPost = RidingPost.builder()
			.leader(otherUser)
			.ridingMainSection(new RidingMainSection("한강 라이딩 하실분", "4시간",
				LocalDateTime.of(2022, 12, 22, 22, 22), 2000,
				new AddressCode(11010), List.of("시작점", "도착지점"), new Coordinate(37.660666, 126.229333)))
			.ridingConditionSection(new RidingConditionSection(INTERMEDIATE))
			.ridingParticipantSection(new RidingParticipantSection(0, 10))
			.build();

		otherRidingPost.addParticipant(otherUser);

		RidingPost anotherPost = RidingPost.builder()
			.leader(user)
			.ridingMainSection(new RidingMainSection("한강 라이딩 하실분", "4시간",
				LocalDateTime.of(2022, 12, 22, 22, 22), 2000,
				new AddressCode(11010), List.of("시작점", "도착지점"), new Coordinate(37.660666, 126.229333)))
			.ridingConditionSection(new RidingConditionSection(BEGINNER))
			.ridingParticipantSection(new RidingParticipantSection(0, 10))
			.build();

		ridingPostRepository.save(ridingPost);
		ridingPostRepository.save(otherRidingPost);
		ridingPostRepository.save(anotherPost);

		em.flush();
		em.clear();

		//When
		UserProfilePageInfo info = userReadService.getUserProfilePageInfo(user.getId());

		String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(info);
		System.out.println(s);

		//Then
		Assertions.assertThat(info.getRidings().getFinished().size()).isEqualTo(0);
		Assertions.assertThat(info.getRidings().getScheduled().size()).isEqualTo(2);
		Assertions.assertThat(info.getRidings().getLeading().size()).isEqualTo(2);
	}
}