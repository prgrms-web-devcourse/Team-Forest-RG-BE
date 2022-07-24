package com.prgrms.rg;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppTest {

	@Autowired
	DataSource dataSource;

	@Test
	void check_database_connection() throws SQLException {
		assertThat(dataSource.getConnection()).isNotNull();
	}
}
