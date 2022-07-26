package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;

import lombok.NoArgsConstructor;

//TODO: Kakao 회원가입과 연동
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ProfileImage extends AttachedImage {

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public ProfileImage(TemporaryImage storedImage, User user) {
		super(storedImage.getId(), storedImage.getOriginalFileName(), storedImage.getUrl());
		this.user = user;
	}

	@Override
	public ImageOwner getAttached() {
		return user;
	}
}
