package com.prgrms.rg.domain.ridingpost.model.dummy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.prgrms.rg.domain.common.file.StoredFile;

import lombok.Getter;

@Entity
@Getter
public class RidingImage extends StoredFile {

	@Column(name = "post_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private RidingPost post;

	private boolean isThumbnail;
}
