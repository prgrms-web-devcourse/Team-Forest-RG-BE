package com.prgrms.rg.domain.ridingpost.application.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.ridingpost.model.RidingSearchCondition;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingSearchFailException;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@SpringBootTest
@Transactional
class RidingPostReadServiceImplTest {
	@Autowired
	RidingPostReadService readService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ObjectMapper mapper;
	@Autowired
	UserReadService userReadService;
	@Autowired
	private RidingPostService ridingPostService;
	
	@BeforeEach
	void init() {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@DisplayName("라이딩 모집 게시글 상세 조회(필수 항목만 입력)")
	@Test
	@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
	void RidingDetailInquiryTest() throws Exception {
		//given
		String title = "testTitle";
		String estimatedTime = "120";
		LocalDateTime ridingDate = LocalDateTime.now().plusDays(10L);
		int fee = 0;
		AddressCode addressCode = new AddressCode(11010);
		int minPart = 4;
		int maxPart = 10;
		String level = RidingLevel.BEGINNER.getLevelName();
		List<String> bicycle = List.of("MTB");
		List<String> routes = List.of("start", "end");
		User leader = TestEntityDataFactory.createUser();
		long userId = userRepository.save(leader).getId();
		var mainCreateCommand = RidingMainSaveCommand.builder()
			.title(title)
			.estimatedTime(estimatedTime)
			.ridingDate(ridingDate)
			.fee(fee)
			.addressCode(addressCode)
			.routes(routes)
			.build();

		var createCommand = new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(minPart, maxPart),
			new RidingConditionSaveCommand(level, bicycle), null
		);
		Long savedPostId = ridingPostService.createRidingPost(userId, createCommand);

		//when
		RidingPostInfo ridingPostInfo = readService.loadRidingPostInfoById(savedPostId);
		em.flush();
		em.clear();

		//then
		RidingPostInfo.LeaderInfo leaderInfo = ridingPostInfo.getLeaderInfo();
		RidingPostInfo.RidingInfo ridingInfo = ridingPostInfo.getRidingInfo();

		assertThat(leaderInfo.getId()).isEqualTo(leader.getId());
		assertThat(leaderInfo.getNickname()).isEqualTo(leader.getNickname());
		assertThat(leaderInfo.getProfileImage()).isEqualTo(leader.getImage().getFileUrl());
		assertThat(ridingInfo.getTitle()).isEqualTo(title);
		assertThat(ridingInfo.getEstimatedTime()).isEqualTo(estimatedTime);
		assertThat(ridingInfo.getRidingDate()).isEqualTo(ridingDate);
		assertThat(ridingInfo.getFee()).isEqualTo(fee);
		assertThat(ridingInfo.getZone().getCode()).isEqualTo(addressCode.getCode());
		assertThat(ridingInfo.getRidingCourses()).contains("start", "end");
		System.out.println(mapper.writeValueAsString(ridingPostInfo));
	}

	@DisplayName("라이딩 검색 조건 중 잘못된 bicycleCode로 요청한다면 RidingSearchFailException이 터저야함")
	@Test
	void test() {
		//given
		Long invalidBicycleCode = 999999L;
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setBicycleCode(invalidBicycleCode);

		assertThrows(RidingSearchFailException.class,
			() -> readService.loadFilteredRidingPostByCondition(condition, PageRequest.of(0, 5)));
	}

	@DisplayName("라이딩 검색 조건 중 잘못된 addressCode로 요청한다면 RidingSearchFailException이 터저야함")
	@Test
	void test2() {
		//given
		int invalidAddressCode = 999999;
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setAddressCode(invalidAddressCode);

		assertThrows(RidingSearchFailException.class,
			() -> readService.loadFilteredRidingPostByCondition(condition, PageRequest.of(0, 5)));
	}

	@DisplayName("라이딩 검색 조건 중 잘못된 RidingStatus로 요청한다면 RidingSearchFailException이 터저야함")
	@Test
	void test3() {
		//given
		Long invalidStatusCode = 999999L;
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setRidingStatusCode(invalidStatusCode);

		assertThrows(RidingSearchFailException.class,
			() -> readService.loadFilteredRidingPostByCondition(condition, PageRequest.of(0, 5)));
	}

	@DisplayName("라이딩 검색 조건 중 잘못된 RidingLevel로 요청한다면 RidingSearchFailException이 터저야함")
	@Test
	void test4() {
		//given
		String invalidRidingLevel = "최최최최상";
		RidingSearchCondition condition = new RidingSearchCondition();
		condition.setRidingLevel(invalidRidingLevel);

		assertThrows(RidingSearchFailException.class,
			() -> readService.loadFilteredRidingPostByCondition(condition, PageRequest.of(0, 5)));
	}
}