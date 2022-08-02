package com.prgrms.rg.web.user.results;

import com.prgrms.rg.domain.user.model.information.UserImageInfo;

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

	public static ImageInfoResult of(UserImageInfo image) {
		return new ImageInfoResult(image.getFileUrl(), image.getOriginalFilename());
	}
}
