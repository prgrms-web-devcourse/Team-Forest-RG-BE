package com.prgrms.rg.domain.ridingpost.model;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.prgrms.rg.domain.common.file.model.ImageOwner;
import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.TemporaryImage;
import com.prgrms.rg.domain.common.model.BaseTimeEntity;
import com.prgrms.rg.domain.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RidingPost extends BaseTimeEntity implements ImageOwner {

	private static final String DEFAULT_IMAGE_URL = "https://programmers.co.kr/assets/icons/apple-icon-6eafc2c4c58a21aef692d6e44ce99d41f999c71789f277317532d0a9c6db8976.png";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "host_id", nullable = false)
	@ManyToOne(optional = false, fetch = LAZY)
	private User leader;

	@OneToOne(mappedBy = "post")
	private RidingThumbnailImage thumbnail;

	@Embedded
	private RidingMainSection ridingMainSection;

	@Embedded
	private RidingParticipantSection ridingParticipantSection;

	@Embedded
	private RidingConditionSection ridingConditionSection;

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "post")
	private List<RidingSubSection> subSectionList = new ArrayList<>();

	@Builder
	public RidingPost(User leader,
		RidingMainSection ridingMainSection,
		RidingParticipantSection ridingParticipantSection,
		RidingConditionSection ridingConditionSection
		) {
		this.ridingMainSection = ridingMainSection;
		this.ridingParticipantSection = ridingParticipantSection;
		this.ridingConditionSection = ridingConditionSection;
		assignLeader(leader);
	}

	private void assignLeader(User leader) {
		this.leader = leader;
		addParticipant(leader);
	}

	public void assignConditionSection(RidingConditionSection ridingConditionSection) {
		this.ridingConditionSection = ridingConditionSection;
	}

	public void addSubSection(RidingSubSection subSection) {
		subSectionList.add(subSection);
		subSection.assignPost(this);
	}

	public void addParticipant(User participant) {
		ridingParticipantSection.addParticipant(this, participant);
	}

	public String getThumbnail() {
		return (thumbnail != null) ? thumbnail.getUrl() : DEFAULT_IMAGE_URL;
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
	public AttachedImage attach(TemporaryImage storedImage) {
		var image = new RidingThumbnailImage(storedImage.getId(), storedImage.getOriginalFileName(), storedImage.getUrl(), this);
		this.thumbnail = image;
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.thumbnail = null;
	}
}
