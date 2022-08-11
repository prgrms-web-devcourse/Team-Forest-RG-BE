package com.prgrms.rg.domain.ridingpost.model;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.rg.domain.common.model.metadata.BicycleRepository;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RidingSearchConditionValidator {
	private final AddressCodeRepository addressCodeRepository;
	private final BicycleRepository bicycleRepository;

	public void validate(RidingSearchCondition condition) {
		validateAddressCode(condition);
		validateBicycleCode(condition);
		validateRidingLevel(condition);
	}

	private void validateAddressCode(RidingSearchCondition condition) {
		Integer addressCode = condition.getAddressCode();
		if (addressCode == null)
			return;
		if (!addressCodeRepository.existsByCode(addressCode))
			throw new IllegalArgumentException("address code '" + addressCode + "' is invalid");
	}

	private void validateBicycleCode(RidingSearchCondition condition) {
		Long bicycleCode = condition.getBicycleCode();
		if (bicycleCode == null)
			return;
		if (!bicycleRepository.existsById(bicycleCode))
			throw new IllegalArgumentException("bicycle code '" + bicycleCode + "' is invalid");
	}

	private void validateRidingLevel(RidingSearchCondition condition) {
		String stringLevel = condition.getRidingLevel();
		if (stringLevel == null)
			return;
		boolean isValid = Arrays.stream(RidingLevel.values())
			.anyMatch(ridingLevel -> stringLevel.equals(ridingLevel.getLevelName()));

		if (!isValid)
			throw new IllegalArgumentException("Riding level '" + stringLevel + "' is invalid");
	}

}
