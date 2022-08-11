package com.prgrms.rg.domain.user.model.information;

import lombok.Getter;

@Getter
public class ContactInfo {

	private String phoneNumber;
	private String email;

	public ContactInfo(String phoneNumber, String email) {
		this.phoneNumber = phoneNumber;
		this.email = email;
	}
}
