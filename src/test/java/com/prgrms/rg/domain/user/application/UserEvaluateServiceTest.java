package com.prgrms.rg.domain.user.application;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.ridingpost.model.RidingPostRepository;
import com.prgrms.rg.domain.user.application.command.ParticipantEvaluateCommand;
import com.prgrms.rg.domain.user.model.UserRepository;

@SpringBootTest
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql", "classpath:evaluation_data.sql"})
@Transactional
class UserEvaluateServiceTest {

	@Autowired
	private RidingPostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserEvaluateService userEvaluateService;

	@Autowired
	private EntityManager em;

	Long leaderId = 10L;
	Long member1Id = 2L;
	Long member2Id = 3L;
	Long ridingpostId = 1L;

	@Test
	@DisplayName("리더의 평가 테스트")
	void evaluateFromLeaderTest() {

		//given
		em.flush();
		em.clear();

		var commandList = List.of(
			new ParticipantEvaluateCommand(member1Id, true, false),
			new ParticipantEvaluateCommand(member2Id, false, false));

		//when
		userEvaluateService.evaluateMembers(leaderId, ridingpostId, commandList);

		em.flush();
		em.clear();

		var updatedRiding = postRepository.findById(ridingpostId);
		assertThat(updatedRiding.isPresent(), is(true));
		var participants = updatedRiding.get().getRidingParticipantSection().getParticipants();
		assertThat(participants, is(hasSize(3)));
		var findUser = userRepository.findById(participants.get(1).getUser().getId());
		assertThat(findUser.isPresent(), is(true));
		assertThat(findUser.get().getMannerInfo().getMannerPoint(), is(equalTo(110)));
	}

	@Test
	@DisplayName("멤버의 평가 리스트")
	void evaluateFromMemerTest() {
		//given

		em.flush();
		em.clear();

		var commandList = List.of(
			new ParticipantEvaluateCommand(leaderId, true, false),
			new ParticipantEvaluateCommand(member2Id, false, false));

		//when
		userEvaluateService.evaluateMembers(member1Id, ridingpostId, commandList);

		em.flush();
		em.clear();

		//then
		var updatedRiding = postRepository.findById(ridingpostId);
		assertThat(updatedRiding.isPresent(), is(true));
		var participants = updatedRiding.get().getRidingParticipantSection().getParticipants();
		assertThat(participants, is(hasSize(3)));
		var findUser = userRepository.findById(participants.get(0).getUser().getId());
		assertThat(findUser.isPresent(), is(true));
		assertThat(findUser.get().getMannerInfo().getMannerPoint(), is(equalTo(110)));
	}
}