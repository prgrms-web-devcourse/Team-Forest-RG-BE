package com.prgrms.rg.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.ImageAttachable;
import com.prgrms.rg.domain.common.file.model.StoredFile;
import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.user.model.information.MannerInfo;
import com.prgrms.rg.domain.user.model.information.RiderInfo;
import com.prgrms.rg.domain.user.model.information.UserImageInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseTimeEntity implements ImageAttachable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Embedded
	private Nickname nickname;

	@Embedded
	private RiderProfile profile;

	@OneToOne(mappedBy = "user")
	private ProfileImage profileImage;

	//TODO: 연락처 추가

	@Embedded
	private Introduction introduction;

	@Embedded
	private Manner manner;

	public boolean addBicycle(Bicycle bicycle) {
		return profile.addBicycle(this, bicycle);
	}

	public Long getId() {
		return this.id;
	}

	public String getNickname() {
		return nickname.get();
	}

	public UserImageInfo getImage() {
		return new UserImageInfo(profileImage.getUrl(), profileImage.getOriginalFileName());
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
			", profileImage=" + profileImage +
			", introduction=" + introduction +
			", manner=" + manner +
			'}';
	}

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		profileImage = new ProfileImage(fileName, fileUrl, this);
		return profileImage;
	}

	@Override
	public void removeCurrentImage() {
		profileImage = null;
	}
}
