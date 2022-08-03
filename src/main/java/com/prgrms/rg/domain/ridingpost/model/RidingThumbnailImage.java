package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.StoredFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RidingThumbnailImage extends StoredFile {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private RidingPost post;

	//TODO storedFile
	public RidingThumbnailImage(Long imageId, RidingPost post) {
		super(imageId);
		this.post = post;
	}

	public RidingThumbnailImage(String originalFileName, String url, RidingPost post) {
		super(originalFileName, url);
		this.post = post;
	}
}
