package com.prgrms.rg.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.Set;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.common.model.metadata.Bicycle;
import com.prgrms.rg.domain.common.model.metadata.RidingLevel;
import com.prgrms.rg.domain.ridingpost.model.AddressCode;
import com.prgrms.rg.domain.user.model.dto.UserRegisterDTO;
import com.prgrms.rg.domain.user.model.information.ContactInfo;
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

	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
	private ProfileImage profileImage;

	@Embedded
	private Introduction introduction;

	private String provider;

	private String providerId;

	private boolean isRegistered;

	private String phoneNumber;

	private String email;

	@JoinColumn(name = "address_code")
	@ManyToOne(fetch = FetchType.LAZY)
	private AddressCode addressCode;

	@Embedded
	private Manner manner;

	public void updateByRegistration(UserRegisterDTO userRegisterDTO) {
		this.nickname = new Nickname(userRegisterDTO.getNickName());

		this.changeRiderProfile(userRegisterDTO.getRidingStartYear(),
			RidingLevel.of(userRegisterDTO.getLevel()), this.profile.getBicycles());

		this.addressCode = userRegisterDTO.getFavoriteRegionCode();
		this.isRegistered = true;
		setPhoneNumber(userRegisterDTO.getPhoneNumber());
	}

	public void setPhoneNumber(String phoneNumber) {
		if (!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phoneNumber))
			throw new IllegalArgumentException("잘못된 번호입니다.");
		this.phoneNumber = phoneNumber;
	}

	public void changeNickname(Nickname nicknameToChange) {
		this.nickname = nicknameToChange;
	}

	public void changeRiderProfile(int ridingYears, RidingLevel level, Set<UserBicycle> bicyclesToApply) {
		this.profile.update(ridingYears, level, bicyclesToApply);
	}

	public void changeIntroduction(Introduction introduction) {
		this.introduction = introduction;
	}

	public void changeAddress(AddressCode addressCode) {
		this.addressCode = addressCode;
	}

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
		return (introduction != null) ? introduction.get() : "";
	}

	public Integer getRegionCode() {
		if (addressCode==null) {
			return null;
		}
		return addressCode.getCode();
	}

	public RiderInfo getRiderInformation() {
		return profile.information();
	}

	public MannerInfo getMannerInfo() {
		return manner.information();
	}

	public ContactInfo getContactInfo() {
		return new ContactInfo(phoneNumber, email);
	}

	public boolean isNewImage(Long imageId) {
		if (profileImage==null) {
			return true;
		}
		return !imageId.equals(profileImage.getId());
	}

	public String getAddressCodeInfo() {
		return addressCode.getArea();
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
	public AttachedImage attach(TemporaryImage storedImage) {
		profileImage = new ProfileImage(storedImage, this);
		return profileImage;
	}

	@Override
	public void removeCurrentImage() {
		profileImage = null;
	}
}
