package com.prgrms.rg.domain.ridingpost.model;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressCodeFinder {

	private final AddressCodeRepository addressCodeRepository;

	public AddressCode find(int code) {
		return addressCodeRepository.findByCode(code).orElseThrow(IllegalArgumentException::new);
	}
}
