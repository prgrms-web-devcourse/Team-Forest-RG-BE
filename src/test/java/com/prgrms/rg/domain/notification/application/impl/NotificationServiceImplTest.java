package com.prgrms.rg.domain.notification.application.impl;

import static com.prgrms.rg.testutil.TestEntityDataFactory.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.prgrms.rg.domain.notification.application.NotificationService;
import com.prgrms.rg.domain.notification.model.NotificationInfo;
import com.prgrms.rg.domain.notification.model.NotificationRepository;
import com.prgrms.rg.domain.ridingpost.application.RidingPostReadService;
import com.prgrms.rg.domain.ridingpost.application.RidingPostService;
import com.prgrms.rg.domain.ridingpost.application.command.RidingSaveCommand;
import com.prgrms.rg.domain.ridingpost.model.RidingPost;
import com.prgrms.rg.domain.user.model.User;
import com.prgrms.rg.domain.user.model.UserRepository;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:address_code.sql", "classpath:bicycle.sql"})
class NotificationServiceImplTest {
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RidingPostService postService;
	@Autowired
	private RidingPostReadService postReadService;
	@Autowired
	private ObjectMapper mapper;
	private User mj;
	private User other;
	private RidingPost ridingPost;

	@BeforeEach
	void init() {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mj = createUser("mjmjmjmj");
		other = createUser("otherother");
		userRepository.save(mj);
		userRepository.save(other);
		RidingSaveCommand postCommand = createRidingPostCreateCommandWithParticipant(1, 3);
		Long postId = postService.createRidingPost(mj.getId(), postCommand);
		ridingPost = postReadService.loadRidingPostById(postId);
		for (int i = 0; i < 20; i++) {
			notificationService.createRidingJoinNotification(mj.getId(), ridingPost.getId());
		}
		for (int i = 0; i < 15; i++) {
			notificationService.createRidingJoinNotification(other.getId(), ridingPost.getId());
		}
	}

	@DisplayName("페이징 조회 테스트")
	@Test
	void test() throws Exception {
		//when
		Page<NotificationInfo> page = notificationService.loadPagedNotificationByUser(mj.getId(),
			PageRequest.of(0, 10));

		//then
		System.out.println(mapper.writeValueAsString(page));
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalPages()).isEqualTo(1);
	}

	@DisplayName("알림 삭제 테스트")
	@Test
	void test2() {
		//when
		notificationService.deleteAllNotificationByUser(other.getId());
		//then
		assertThat(notificationRepository.count()).isEqualTo(20);
	}

}