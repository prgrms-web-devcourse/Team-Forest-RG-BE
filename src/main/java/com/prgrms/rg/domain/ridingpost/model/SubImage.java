package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubImage extends AttachedImage {

	@ManyToOne(fetch = FetchType.LAZY)
	private RidingSubSection subInformation;

	public SubImage(Long imageId, AttachedImage attachedImage, RidingSubSection subInformation) {
		super(imageId, attachedImage.getOriginalFileName(), attachedImage.getUrl());
		this.subInformation = subInformation;
	}

	public SubImage(Long imageId, TemporaryImage storedImage, RidingSubSection subInformation) {
		super(imageId, storedImage.getOriginalFileName(), storedImage.getUrl());
		this.subInformation = subInformation;
	}

	@Override
	public ImageOwner getAttached() {
		return subInformation;
	}
}
