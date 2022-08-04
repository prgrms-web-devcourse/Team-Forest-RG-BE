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

	public SubImage(TemporaryImage storedImage, RidingSubSection subInformation) {
		super(storedImage.getId(), storedImage.getOriginalFileName(), storedImage.getUrl());
		this.subInformation = subInformation;
	}

	@Override
	public ImageOwner getAttached() {
		return subInformation;
	}
}
