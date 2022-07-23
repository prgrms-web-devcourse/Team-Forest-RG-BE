package com.prgrms.rg;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseTest {

	@Autowired
	DummyRepository repository;

	@Test
	void check_database_status() {
		var dummy = new Dummy();
		dummy.setName("hello");
		dummy = repository.save(dummy);
		assertThat(repository.findById(dummy.getId())).get().extracting("id").isEqualTo(dummy.getId());

	}
}
