package com.prgrms.rg.domain.user.application;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.application.impl.RidingJoinService;
import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.application.command.ParticipantEvaluateCommand;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@SpringBootTest
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql", "classpath:riding_post.sql"})
@Transactional
class UserEvaluateServiceTest {

	@Autowired
	private RidingPostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserEvaluateService userEvaluateService;

	@Autowired
	private RidingJoinService joinService;

	@Autowired
	private EntityManager em;

	User leader;
	User member1;
	User member2;
	Long ridingpostId = 1L;

	@BeforeEach
	void setup() {
		leader = userRepository.save(TestEntityDataFactory.createUser(1L, "leader"));
		member1 = userRepository.save(TestEntityDataFactory.createUser(2L, "memberOne"));
		member2 = userRepository.save(TestEntityDataFactory.createUser(3L, "memberTwo"));

		joinService.joinUserToRiding(member1.getId(), ridingpostId);
		joinService.joinUserToRiding(member2.getId(), ridingpostId);
	}

	//noshow, recommend count 반영 확인
	@Test
	@DisplayName("리더의 평가 테스트")
	void evaluateFromLeaderTest() {

		//given
		em.flush();
		em.clear();

		var commandList = List.of(
			new ParticipantEvaluateCommand(member1.getId(), true, false),
			new ParticipantEvaluateCommand(member2.getId(), false, false));

		//when
		userEvaluateService.evaluateMembers(leader.getId(), ridingpostId, commandList);

		em.flush();
		em.clear();

		var updatedRiding = postRepository.findById(ridingpostId);
		assertThat(updatedRiding.isPresent(), is(true));
		var participants = updatedRiding.get().getRidingParticipantSection().getParticipants();
		assertThat(participants, is(hasSize(3)));
		var findUser = userRepository.findById(participants.get(1).getUser().getId());
		assertThat(findUser.isPresent(), is(true));
		assertThat(findUser.get().getMannerInfo().getMannerPoint(), is(equalTo(1)));
	}

	@Test
	@DisplayName("멤버의 평가 리스트")
	void evaluateFromMemerTest() {
		//given

		em.flush();
		em.clear();

		var commandList = List.of(
			new ParticipantEvaluateCommand(leader.getId(), true, false),
			new ParticipantEvaluateCommand(member2.getId(), false, false));

		//when
		userEvaluateService.evaluateMembers(member1.getId(), ridingpostId, commandList);

		em.flush();
		em.clear();

		//then
		var updatedRiding = postRepository.findById(ridingpostId);
		assertThat(updatedRiding.isPresent(), is(true));
		var participants = updatedRiding.get().getRidingParticipantSection().getParticipants();
		assertThat(participants, is(hasSize(3)));
		var findUser = userRepository.findById(participants.get(0).getUser().getId());
		assertThat(findUser.isPresent(), is(true));
		assertThat(findUser.get().getMannerInfo().getMannerPoint(), is(equalTo(1)));
	}
}