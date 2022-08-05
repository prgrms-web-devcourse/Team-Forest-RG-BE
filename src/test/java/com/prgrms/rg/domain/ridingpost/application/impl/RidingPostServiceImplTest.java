package com.prgrms.rg.domain.ridingpost.application.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainCreateCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantCreateCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;

@SpringBootTest
@Transactional
class RidingPostServiceImplTest {

	@Autowired
	private RidingPostServiceImpl ridingPostService;

	@Autowired
	private RidingPostRepository ridingPostRepository;

	@Test
	@DisplayName("사진x RidingPost 생성")
	@Sql(scripts = "classpath:data.sql")
	void createRidingTest(){

	    //given
		List<String> routes = List.of("start", "end");
		var mainCreateCommand = RidingMainCreateCommand.builder()
			.title("testTitle")
			.estimatedTime(120).ridingDate(LocalDateTime.now().plusDays(10L))
			.fee(0).addressCode(new AddressCode(11010)).routes(routes).build();

		var createCommand = new RidingCreateCommand(null,
			mainCreateCommand,
			new RidingParticipantCreateCommand(6, 10),
			new RidingConditionCreateCommand("BEGINNER", List.of("MTV")), null
		);

	    //when
		Long savedPostId = ridingPostService.createRidingPost(1L, createCommand);
		RidingPost savedOne = ridingPostRepository.findById(savedPostId).get();

	    //then
		assertThat(savedOne.getRidingMainSection(), is(samePropertyValuesAs(mainCreateCommand.toSection())));
	}
}