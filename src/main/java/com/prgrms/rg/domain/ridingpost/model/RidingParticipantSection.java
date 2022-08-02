package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Range;

import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class RidingParticipantSection {

	@Column(name = "participant_count")
	private int participantCount = 1;

	@Range(min = 5, max = 30)
	@Column(name = "max_participant_count", nullable = false)
	private int maxParticipantCount;

	@Range(min = 5, max = 30)
	@Column(name = "min_participant_count", nullable = false)
	private int minParticipantCount;

	@Column(name = "riding_status")
	@Enumerated(EnumType.STRING)
	private RidingStatus status = RidingStatus.IN_PROGRESS;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<RidingParticipant> participants = new ArrayList<>();

	public RidingParticipantSection(int maxParticipantCount, int minParticipantCount) {
		setMinMaxParticipantCount(minParticipantCount, maxParticipantCount);
	}

	public void addParticipant(RidingPost post, User participant) {
		//TODO 2차 시기에 동시성 처리
		participants.add(new RidingParticipant(post, participant));
		addParticipantCount();
		updateStatus();
	}

	private void addParticipantCount() {
		checkArgument(participantCount != maxParticipantCount);
		participantCount++;
	}

	private void updateStatus() {
		if (participantCount >= maxParticipantCount) {
			status = RidingStatus.CLOSING;
		} else {
			status = RidingStatus.IN_PROGRESS;
		}
	}

	private void setMinMaxParticipantCount(int minParticipantCount, int maxParticipantCount) {
		checkArgument(minParticipantCount <= maxParticipantCount
			&& maxParticipantCount <= participantCount);

		this.minParticipantCount = minParticipantCount;
		this.maxParticipantCount = maxParticipantCount;
		updateStatus();
	}

}
