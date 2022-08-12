package com.prgrms.rg.domain.ridingpost.application.impl;

import static com.prgrms.rg.testutil.TestEntityDataFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.ridingpost.model.RidingStatus;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinCancelEvent;
import com.prgrms.rg.domain.ridingpost.model.event.RidingJoinEvent;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinCancelFailException;
import com.prgrms.rg.domain.ridingpost.model.exception.RidingJoinFailException;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

@SpringBootTest
@Transactional
@RecordApplicationEvents
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
class RidingJoinServiceTest {
	@Autowired
	ApplicationEvents events;
	User leader;
	User participant;
	Long postId;
	@Autowired
	private RidingJoinService joinService;
	@Autowired
	private RidingPostService ridingService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RidingPostReadService postReadService;

	@BeforeEach
	void init() {
		leader = userRepository.save(createUser("leader"));
	}

	@DisplayName("참가 신청이 되면 해당 라이딩 게시글에 참가자가 늘어나고 RidingJoinEvent가 발행되어야한다.")
	@Test
	void test() {
		//given
		RidingSaveCommand command = createRidingPostCreateCommandWithParticipant(1, 3);
		postId = ridingService.createRidingPost(leader.getId(), command);
		participant = userRepository.save(createUser("participant"));

		//when
		joinService.joinUserToRiding(participant.getId(), postId);

		//then
		RidingPost post = postReadService.loadRidingPostById(postId);
		int count = (int)events.stream(RidingJoinEvent.class).count();
		assertThat(post.getRidingParticipantSection().getParticipants()).hasSize(2);
		assertThat(count).isEqualTo(1);
	}

	@DisplayName("참가 신청이 되어 최대 인원에 도달하면 게시글 상태는 close로 바뀌어야 한다.")
	@Test
	void test2() {
		//given
		RidingSaveCommand command = createRidingPostCreateCommandWithParticipant(1, 3);
		postId = ridingService.createRidingPost(leader.getId(), command);
		participant = userRepository.save(createUser("participant"));
		User participant2 = userRepository.save(createUser("participanttwo"));

		//when
		joinService.joinUserToRiding(participant.getId(), postId);
		joinService.joinUserToRiding(participant2.getId(), postId);

		//then
		RidingPost post = postReadService.loadRidingPostById(postId);
		assertThat(post.getRidingParticipantSection().getStatus()).isEqualTo(RidingStatus.CLOSED);
	}

	@DisplayName("이미 참가중인 라이더가 요청할 경우 RidingJoinFailException 발생시킨다.")
	@Test
	void test3() {
		//given
		RidingSaveCommand command = createRidingPostCreateCommandWithParticipant(1, 3);
		postId = ridingService.createRidingPost(leader.getId(), command);
		participant = userRepository.save(createUser("participant"));

		//when
		joinService.joinUserToRiding(participant.getId(), postId);

		//then
		assertThrows(RidingJoinFailException.class, () -> joinService.joinUserToRiding(participant.getId(), postId));
	}

	@DisplayName("참가 취소 시 해당 라이딩 참여자가 줄고 RidingJoinCancelEvent 발행되어야한다.")
	@Test
	void test4() {
		//given
		RidingSaveCommand command = createRidingPostCreateCommandWithParticipant(1, 3);
		postId = ridingService.createRidingPost(leader.getId(), command);
		participant = userRepository.save(createUser("participant"));
		joinService.joinUserToRiding(participant.getId(), postId);

		//when
		joinService.cancelJoin(participant.getId(), postId);

		//then
		RidingPost post = postReadService.loadRidingPostById(postId);
		int count = (int)events.stream(RidingJoinCancelEvent.class).count();
		assertThat(post.getRidingParticipantSection().getParticipants()).hasSize(1);
		assertThat(count).isEqualTo(1);
	}

	@DisplayName("모집 마감된 라이딩에서 탈주자가 생기면 모집 상태가 open으로 바뀌어야한다.")
	@Test
	void test5() {
		//given
		RidingSaveCommand command = createRidingPostCreateCommandWithParticipant(1, 2);
		postId = ridingService.createRidingPost(leader.getId(), command);
		participant = userRepository.save(createUser("participant"));
		joinService.joinUserToRiding(participant.getId(), postId);
		RidingPost post = postReadService.loadRidingPostById(postId);
		System.out.println("current progress" + post.getRidingParticipantSection().getStatus());

		//when
		joinService.cancelJoin(participant.getId(), postId);

		//then

		assertThat(post.getRidingParticipantSection().getStatus()).isEqualTo(RidingStatus.IN_PROGRESS);
	}

	@DisplayName("참가자가 아닌 라이더가 참가 취소 요청을 할 경우 RidingJoinCancelFailException 발생시킨다.")
	@Test
	void test6() {
		//given
		RidingSaveCommand command = createRidingPostCreateCommandWithParticipant(1, 2);
		postId = ridingService.createRidingPost(leader.getId(), command);
		User user = userRepository.save(createUser("participant"));

		//when then
		assertThrows(RidingJoinCancelFailException.class, () -> joinService.cancelJoin(user.getId(), postId));
	}
}