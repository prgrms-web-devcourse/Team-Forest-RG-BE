package com.prgrms.rg.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.user.model.information.MannerInfo;
import com.prgrms.rg.domain.user.model.information.RiderInfo;
import com.prgrms.rg.domain.user.model.information.UserImageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Getter
public class User extends BaseTimeEntity implements ImageOwner {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Embedded
	private Nickname nickname;

	@Embedded
	private RiderProfile profile;

	private String profileImages;

	@OneToOne(mappedBy = "user")
	private ProfileImage profileImage;

	//TODO: 연락처 추가

	@Embedded
	private Introduction introduction;

	private String provider;

	private String providerId;

	private Boolean isRegistered;

	@Embedded
	private Manner manner;

	public boolean addBicycle(Bicycle bicycle) {
		return profile.addBicycle(this, bicycle);
	}

	public String getNickname() {
		return nickname.get();
	}

	public UserImageInfo getImage() {
		return (profileImage != null) ? new UserImageInfo(profileImage.getUrl(), profileImage.getOriginalFileName())
			: UserImageInfo.defaultImage();
	}

	public String getIntroduction() {
		return introduction.get();
	}

	public RiderInfo getRiderInformation() {
		return profile.information();
	}

	public MannerInfo getMannerInfo() {
		return manner.information();
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", nickname=" + nickname +
			", profile=" + profile +
			", profileImage=" + profileImages +
			", introduction=" + introduction +
			", manner=" + manner +
			'}';
	}

	@Override
	public AttachedImage attach(TemporaryImage storedImage) {
		profileImage = new ProfileImage(storedImage, this);
		return profileImage;
	}

	@Override
	public void removeCurrentImage() {
		profileImages = null;
	}
}
