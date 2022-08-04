package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RidingThumbnailImage extends AttachedImage {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private RidingPost post;

	public RidingThumbnailImage(Long id, String originalFileName, String url,
		RidingPost post) {
		super(id, originalFileName, url);
		this.post = post;
	}

	@Override
	public ImageOwner getAttached() {
		return post;
	}
}
