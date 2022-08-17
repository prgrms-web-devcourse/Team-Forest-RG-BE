package com.prgrms.rg.domain.user.model.information;

import lombok.Getter;

@Getter
public class UserImageInfo {

	private static final String DEFAULT_IMAGE_URL = "https://team-05-storage.s3.ap-northeast-2.amazonaws.com/static/RG_Logo.png";
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
