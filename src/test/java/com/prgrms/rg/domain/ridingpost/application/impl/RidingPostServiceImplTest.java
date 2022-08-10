package com.prgrms.rg.domain.ridingpost.application.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.application.command.RidingConditionSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingMainSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingParticipantSaveCommand;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSubSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.model.Manner;
import com.prgrms.rg.domain.user.model.Nickname;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.testutil.TestEntityDataFactory;

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
		var mainCreateCommand = RidingMainSaveCommand.builder()
			.title("testTitle")
			.estimatedTime("2시간").ridingDate(LocalDateTime.now().plusDays(10L))
			.fee(0).addressCode(new AddressCode(11010)).routes(routes).build();

		var createCommand = new RidingSaveCommand(null,
			mainCreateCommand,
			new RidingParticipantSaveCommand(6, 10),
			new RidingConditionSaveCommand(RidingLevel.BEGINNER.getLevelName(), List.of("MTB")), null
		);

	    //when
		Long savedPostId = ridingPostService.createRidingPost(user.getId(), createCommand);
		var savedOne = ridingPostRepository.findById(savedPostId);

	    //then
		assertThat(savedOne.isPresent(), is(true));
		assertThat(savedOne.get().getRidingMainSection(), is(samePropertyValuesAs(mainCreateCommand.toSection())));
	}

	@Test
	@Transactional
	@DisplayName("ridingpost 수정 테스트")
	@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
	void updateRidingTest(){
		//user-post
		User leader = userRepository.save(TestEntityDataFactory.createUser());
		RidingPost post = ridingPostRepository.save(TestEntityDataFactory.createRidingPost(leader.getId()));

		var conditionCommand = new RidingConditionSaveCommand("상", List.of("MTB"));
		var participantCommand = new RidingParticipantSaveCommand(post.getRidingParticipantSection().getMinParticipantCount(),
			post.getRidingParticipantSection().getMaxParticipantCount());
		var subCommand = new RidingSubSaveCommand("new-sub title", "new-sub content",
			Collections.emptyList());

		var mainCommand = RidingMainSaveCommand.builder()
			.title("자전거가 타고싶어요")
			.ridingDate(LocalDateTime.now().plusDays(10L))
			.routes(List.of("중앙 공원", "능골 공원", "탑골 공원"))
			.estimatedTime("3시간")
			.addressCode(new AddressCode(11010))
			.fee(10000)
			.departurePlace(new Coordinate(35.232600, 127.650250)).build();

		var updateCommand = new RidingSaveCommand(null, mainCommand, participantCommand, conditionCommand, List.of(subCommand));

		//when
		ridingPostService.updateRidingPost(leader.getId(), post.getId(), updateCommand);

		var updatedPost = ridingPostRepository.findById(post.getId());

		assertThat(updatedPost.isPresent(), is(true));
		assertThat(updatedPost.get().getLeader().getId(), is(equalTo(leader.getId())));
		assertThat(updatedPost.get().getRidingMainSection().getEstimatedTime(), is(equalTo(
			mainCommand.getEstimatedTime())));
		assertThat(updatedPost.get().getSubSectionList(), is(hasSize(1)));
		assertThat(updatedPost.get().getSubSectionList().get(0).getTitle(), is(equalTo(subCommand.getTitle())));

	}
}