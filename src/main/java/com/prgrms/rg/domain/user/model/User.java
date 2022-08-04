package com.prgrms.rg.domain.user.model;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.Collection;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User extends BaseTimeEntity implements UserDetails, ImageOwner {

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

	private String isRegistered;

	@Embedded
	private Manner manner;

	public boolean addBicycle(Bicycle bicycle) {
		return profile.addBicycle(this, bicycle);
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return this.nickname.toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.provider != null && this.providerId != null;
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
