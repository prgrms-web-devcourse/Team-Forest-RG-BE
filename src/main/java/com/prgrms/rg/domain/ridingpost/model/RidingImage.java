package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.file.StoredFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RidingImage extends StoredFile {

	@Column(name = "post_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private RidingPost post;

	@Column(name = "is_thumbnail")
	private boolean isThumbnail;

	public void setThumbnail() {
		isThumbnail = true;
	}

	public RidingImage(String originalFileName, String url, RidingPost post, boolean isThumbnail) {
		super(originalFileName, url);
		this.post = post;
		this.isThumbnail = isThumbnail;
	}
}
