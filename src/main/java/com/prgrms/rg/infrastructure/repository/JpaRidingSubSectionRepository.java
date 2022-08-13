package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.RidingSubSection;
import com.prgrms.rg.domain.ridingpost.model.RidingSubSectionRepository;

public interface JpaRidingSubSectionRepository
	extends RidingSubSectionRepository, JpaRepository<RidingSubSection, Long> {
}
