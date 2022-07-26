package com.prgrms.rg.domain.ridingpost.model;

import java.util.Optional;

public interface AddressCodeRepository {
	Optional<AddressCode> findByCode(int code);

	boolean existsByCode(int code);
	AddressCode save(AddressCode addressCode);
}
