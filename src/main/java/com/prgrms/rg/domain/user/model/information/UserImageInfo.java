package com.prgrms.rg.domain.user.model.information;

import lombok.Getter;

@Getter
public class UserImageInfo {

	private String fileUrl;
	private String originalFilename;

	public UserImageInfo(String fileUrl, String originalFilename) {
		this.fileUrl = fileUrl;
		this.originalFilename = originalFilename;
	}
}
