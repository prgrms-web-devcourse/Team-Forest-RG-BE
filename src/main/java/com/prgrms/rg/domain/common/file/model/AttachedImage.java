package com.prgrms.rg.domain.common.file.model;

import static javax.persistence.GenerationType.*;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.prgrms.rg.infrastructure.file.ImageDeleteEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(ImageDeleteEntityListener.class)
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AttachedImage {

	public AttachedImage(String originalFileName, String url) {
		this.originalFileName = originalFileName;
		this.url = url;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	protected String originalFileName;
	protected String url;
}
