package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.RidingParticipant;
import com.prgrms.rg.domain.ridingpost.model.RidingParticipantRepository;

public interface JpaRidingParticipantRepository
	extends JpaRepository<RidingParticipant, Long>, RidingParticipantRepository {

}
