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

import com.prgrms.rg.domain.common.file.model.AttachedImage;
import com.prgrms.rg.domain.common.file.model.ImageOwner;
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

	private static final String DEFAULT_IMAGE_URL = "https://team-05-storage.s3.ap-northeast-2.amazonaws.com/static/RG_Logo.png";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "host_id", nullable = false)
	@ManyToOne(optional = false, fetch = LAZY)
	private User leader;

	@OneToOne(mappedBy = "post", cascade = ALL)
	private RidingThumbnailImage thumbnail;

	@Embedded
	private RidingMainSection ridingMainSection;

	@Embedded
	private RidingParticipantSection ridingParticipantSection;

	@Embedded
	private RidingConditionSection ridingConditionSection;

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "post", orphanRemoval = true)
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

	public void changePost(RidingPost newPost) {
		removeCurrentSubSection();
		ridingMainSection.update(newPost.getRidingMainSection());
		ridingParticipantSection.update(newPost.getRidingParticipantSection());
		ridingConditionSection.update(newPost.getRidingConditionSection());
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

	public void join(User participant) {
		var ridingStatus = getRidingParticipantSection().getStatus();
		if (ridingStatus != RidingStatus.IN_PROGRESS)
			throw new IllegalArgumentException("riding not in progress");
		addParticipant(participant);
	}

	public void close() {
		ridingParticipantSection.changeRidingStatus(RidingStatus.CLOSED);
	}

	public void addParticipant(User participant) {
		ridingParticipantSection.addParticipant(this, participant);
	}

	public void removeParticipant(User participant) {
		ridingParticipantSection.removeParticipant(participant);
	}

	public String getThumbnail() {
		return (thumbnail != null) ? thumbnail.getUrl() : DEFAULT_IMAGE_URL;
	}

	public Long getThumbnailId() {
		return (thumbnail != null) ? thumbnail.getId() : null;
	}

	public boolean equalToThumbnail(Long thumbnailId) {
		if (this.thumbnail == null)
			return thumbnailId == null;
		return this.thumbnail.getId().equals(thumbnailId);
	}

	public void removeCurrentSubSection() {
		for (int i = 0; i < subSectionList.size(); i++) {
			subSectionList.get(i).removeCurrentImage();
		}
		this.subSectionList.clear();
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
		this.thumbnail = new RidingThumbnailImage(storedImage.getId(), storedImage.getOriginalFileName(),
			storedImage.getUrl(), this);
		return thumbnail;
	}

	@Override
	public void removeCurrentImage() {
		this.thumbnail = null;
	}
}
