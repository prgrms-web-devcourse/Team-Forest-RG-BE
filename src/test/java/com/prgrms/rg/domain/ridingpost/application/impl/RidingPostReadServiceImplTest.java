package com.prgrms.rg.domain.ridingpost.application.impl;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.RidingPostInfo;
import com.prgrms.rg.domain.user.application.UserReadService;
import com.prgrms.rg.domain.user.model.User;

@SpringBootTest
@Transactional
class RidingPostReadServiceImplTest {
	@Autowired
	RidingPostReadService readService;
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
	@Sql(scripts = "classpath:data.sql")
	void RidingDetailInquiryTest() throws Exception {
		//given
		String title = "testTitle";
		int estimatedTime = 120;
		LocalDateTime ridingDate = LocalDateTime.now().plusDays(10L);
		int fee = 0;
		AddressCode addressCode = new AddressCode(11010);
		int minPart = 4;
		int maxPart = 10;
		String level = RidingLevel.BEGINNER.name();
		List<String> bicycle = List.of("MTV");
		List<String> routes = List.of("start", "end");
		User leader = userReadService.getUserEntityById(1L);

		var mainCreateCommand = RidingMainCreateCommand.builder()
			.title(title)
			.estimatedTime(estimatedTime)
			.ridingDate(ridingDate)
			.fee(fee)
			.addressCode(addressCode)
			.routes(routes)
			.build();

		var createCommand = new RidingCreateCommand(null,
			mainCreateCommand,
			new RidingParticipantCreateCommand(minPart, maxPart),
			new RidingConditionCreateCommand(level, bicycle), null
		);
		Long savedPostId = ridingPostService.createRidingPost(1L, createCommand);

		//when
		RidingPostInfo ridingPostInfo = readService.getRidingPostInfoById(savedPostId);

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
		assertThat(ridingInfo.getRidingCourses()).isEqualTo(routes);
		System.out.println(mapper.writeValueAsString(ridingPostInfo));
	}
}