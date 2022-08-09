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
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

@SpringBootTest
@Transactional
class RidingPostServiceImplTest {

	@Autowired
	private RidingPostServiceImpl ridingPostService;

	@Autowired
	private RidingPostRepository ridingPostRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("사진x RidingPost 생성")
	@Transactional
	@Sql(scripts = "classpath:data.sql")
	void createRidingTest(){

	    //given
		var user = User.builder()
			.nickname(new Nickname("testNickname"))
			.manner(Manner.create())
			.build();
		User savedUser = userRepository.save(user);

		List<String> routes = List.of("start", "end");
		var mainCreateCommand = RidingMainCreateCommand.builder()
			.title("testTitle")
			.estimatedTime("2시간").ridingDate(LocalDateTime.now().plusDays(10L))
			.fee(0).addressCode(new AddressCode(11010)).routes(routes).build();

		var createCommand = new RidingCreateCommand(null,
			mainCreateCommand,
			new RidingParticipantCreateCommand(6, 10),
			new RidingConditionCreateCommand("BEGINNER", List.of("MTB")), null
		);

	    //when
		Long savedPostId = ridingPostService.createRidingPost(user.getId(), createCommand);
		var savedOne = ridingPostRepository.findById(savedPostId);

	    //then
		assertThat(savedOne.isPresent(), is(true));
		assertThat(savedOne.get().getRidingMainSection(), is(samePropertyValuesAs(mainCreateCommand.toSection())));
	}
}