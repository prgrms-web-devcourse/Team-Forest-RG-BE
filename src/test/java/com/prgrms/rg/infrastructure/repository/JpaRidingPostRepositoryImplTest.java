package com.prgrms.rg.infrastructure.repository;

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
import com.prgrms.rg.domain.ridingpost.model.RidingStatus;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;
import com.prgrms.rg.testutil.TestEntityDataFactory;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
class JpaRidingPostRepositoryImplTest {

	@Autowired
	private RidingPostRepository ridingPostRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EntityManager em;

	@Test
	@DisplayName("update ridingpostList 테스트")
	void updateListTest(){

		User user = userRepository.save(TestEntityDataFactory.createUser());
		var post1 = ridingPostRepository.save(TestEntityDataFactory.createSimplePost(user.getId()));
		var post2 = ridingPostRepository.save(TestEntityDataFactory.createSimplePost(user.getId()));
		var post3 = ridingPostRepository.save(TestEntityDataFactory.createSimplePost(user.getId()));
		var post4 = ridingPostRepository.save(TestEntityDataFactory.createSimplePost(user.getId()));
		var post5 = ridingPostRepository.save(TestEntityDataFactory.createSimplePost(user.getId()));

		em.flush();
		em.clear();

		var updateList = List.of(post1, post2, post3, post4, post5);
		ridingPostRepository.updateRidingPostListToClosed(updateList);

		em.flush();
		em.clear();

		var findOne = ridingPostRepository.findById(post1.getId());
		assertThat(findOne.isPresent(), is(true));
		assertThat(findOne.get().getRidingParticipantSection().getStatus(), is(equalTo(RidingStatus.CLOSED)));

	}
}