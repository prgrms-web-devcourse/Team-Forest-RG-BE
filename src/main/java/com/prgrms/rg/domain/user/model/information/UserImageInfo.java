package com.prgrms.rg.domain.user.model.information;

import lombok.Getter;

@Getter
public class UserImageInfo {

	private static final String DEFAULT_IMAGE_URL = "https://programmers.co.kr/assets/icons/apple-icon-6eafc2c4c58a21aef692d6e44ce99d41f999c71789f277317532d0a9c6db8976.png";
	private static final String DEFAULT_IMAGE_NAME = "default_profile.png";

	private String fileUrl;
	private String originalFilename;

	public UserImageInfo(String fileUrl, String originalFilename) {
		this.fileUrl = fileUrl;
		this.originalFilename = originalFilename;
	}

	public static UserImageInfo defaultImage() {
		return new UserImageInfo(DEFAULT_IMAGE_URL, DEFAULT_IMAGE_NAME);
	}
}
