package com.prgrms.rg.domain.ridingpost.model;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.common.file.ImageAttachable;
import com.prgrms.rg.domain.common.file.StoredFile;
import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RidingPost extends BaseTimeEntity implements ImageAttachable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "host_id", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User leader;

	@OneToOne(mappedBy = "post")
	private RidingThumbnailImage thumbnail;

	@Embedded
	private RidingMainSection ridingMainSection;

	@Embedded
	private RidingParticipantSection ridingParticipantSection;

	@Embedded
	private RidingConditionSection ridingConditionSection;

	@Builder
	public RidingPost(User leader, RidingMainSection ridingMainSection,
		RidingParticipantSection ridingParticipantSection,
		RidingConditionSection ridingConditionSection) {
		assignLeader(leader);
		this.ridingMainSection = ridingMainSection;
		this.ridingParticipantSection = ridingParticipantSection;
		this.ridingConditionSection = ridingConditionSection;
	}

	private void assignLeader(User leader) {
		this.leader = leader;
		addParticipant(leader);
	}

	public void addParticipant(User participant) {
		ridingParticipantSection.addParticipant(this, participant);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RidingPost))
			return false;
		RidingPost that = (RidingPost)o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		var image = new RidingThumbnailImage(fileName, fileUrl, this);
		this.thumbnail = image;
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.thumbnail = null;
	}
}
