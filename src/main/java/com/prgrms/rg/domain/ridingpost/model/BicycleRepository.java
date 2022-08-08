package com.prgrms.rg.domain.ridingpost.model;

import java.util.Optional;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;

public interface BicycleRepository {

	Optional<Bicycle> findByName(String name);
}
