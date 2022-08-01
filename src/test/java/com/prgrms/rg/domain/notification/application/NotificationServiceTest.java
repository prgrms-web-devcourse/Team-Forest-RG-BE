package com.prgrms.rg.domain.notification.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class NotificationServiceImplTest {
	// 서브스크라이브 호출해서 연결하고
	// 이벤트 찾아서 send한번했을때
	// 메세지가 전송이 됐는지는 어캐아는데
}