package com.prgrms.rg.web.user.results;

import com.prgrms.rg.domain.user.model.ProfileImage;

import lombok.Data;

@Data
public class ImageInfoResult {
	//썸네일 관련 설정
	private String url;
	private String fileName;

	private ImageInfoResult(String url, String fileName) {
		this.url = url;
		this.fileName = fileName;
	}

	public static ImageInfoResult of(ProfileImage image) {
		return new ImageInfoResult(image.getUrl(), image.getOriginalFileName());
	}
}
