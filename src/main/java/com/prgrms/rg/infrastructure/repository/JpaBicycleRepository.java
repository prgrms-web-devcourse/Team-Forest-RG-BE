package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.ridingpost.model.BicycleRepository;

public interface JpaBicycleRepository extends JpaRepository<Bicycle, Long>, BicycleRepository {
}
