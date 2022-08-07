package com.prgrms.rg.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.ridingpost.model.AddressCodeRepository;

public interface JpaAddressCodeRepository extends JpaRepository<AddressCode, Integer>, AddressCodeRepository {
}
