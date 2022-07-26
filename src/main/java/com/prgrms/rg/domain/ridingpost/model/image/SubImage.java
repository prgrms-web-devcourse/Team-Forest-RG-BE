package com.prgrms.rg.domain.ridingpost.model.image;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.ridingpost.model.RidingSubSection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubImage extends AttachedImage {

	@ManyToOne(fetch = FetchType.LAZY)
	private RidingSubSection subInformation;

	public SubImage(Long imageId, TemporaryImage storedImage, RidingSubSection subInformation) {
		super(imageId, storedImage.getOriginalFileName(), storedImage.getUrl());
		this.subInformation = subInformation;
	}

	public void updateOwner(RidingSubSection subInformation) {
		this.subInformation = subInformation;
	}

	@Override
	public ImageOwner getAttached() {
		return subInformation;
	}
}
