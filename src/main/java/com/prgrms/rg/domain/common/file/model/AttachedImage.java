package com.prgrms.rg.domain.common.file.model;

import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.infrastructure.file.ImageDeleteEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(ImageDeleteEntityListener.class)
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AttachedImage extends BaseTimeEntity {

	protected AttachedImage(Long id, String originalFileName, String url) {
		this.id = id;
		this.originalFileName = originalFileName;
		this.url = url;
	}

	@Id
	private Long id;

	protected String originalFileName;
	protected String url;
	public abstract ImageOwner getAttached();
}
