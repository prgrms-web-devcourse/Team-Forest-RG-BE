package com.prgrms.rg.domain.common.model.metadata;

import java.util.List;
import java.util.Optional;

public interface BicycleRepository {
	Optional<Bicycle> findById(Long id);

	Optional<Bicycle> findByName(String name);

	List<Bicycle> findAll();

	String findNameById(Long id);
}
