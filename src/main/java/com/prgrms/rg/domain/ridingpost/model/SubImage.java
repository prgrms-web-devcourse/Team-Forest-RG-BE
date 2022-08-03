package com.prgrms.rg.domain.ridingpost.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.file.model.StoredFile;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubImage extends StoredFile {

	@ManyToOne(fetch = FetchType.LAZY)
	private RidingSubSection subInformation;

	//TODO storedFile
	public SubImage(Long imageId, RidingSubSection subInformation) {
		super(imageId);
		this.subInformation = subInformation;
	}

	public SubImage(String originalFileName, String url, RidingSubSection subInformation) {
		super(originalFileName, url);
		this.subInformation = subInformation;
	}
}
