package com.prgrms.rg.domain.ridingpost.model;

import static com.google.common.base.Preconditions.*;
import static com.prgrms.rg.domain.ridingpost.model.RidingStatus.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.Range;

import com.prgrms.rg.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
public class RidingParticipantSection {

	@Column(name = "participant_count")
	private int participantCount = 1;

	@Range(min = 5, max = 30)
	@Column(name = "min_participant_count", nullable = false)
	private int minParticipantCount;

	@Range(min = 5, max = 30)
	@Column(name = "max_participant_count", nullable = false)
	private int maxParticipantCount;

	@Column(name = "riding_status")
	@Enumerated(EnumType.STRING)
	private RidingStatus status = IN_PROGRESS;
	@OneToMany(mappedBy = "post", cascade = ALL, fetch = LAZY, orphanRemoval = true)
	private List<RidingParticipant> participants = new ArrayList<>();

	public RidingParticipantSection(int minParticipantCount, int maxParticipantCount) {
		setMinMaxParticipantCount(minParticipantCount, maxParticipantCount);
	}

	public void update(RidingParticipantSection section) {
		setMinMaxParticipantCount(section.getMinParticipantCount(), section.getMaxParticipantCount());
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
			status = RidingStatus.CLOSED;
		} else {
			status = IN_PROGRESS;
		}
	}

	private void setMinMaxParticipantCount(int minParticipantCount, int maxParticipantCount) {
		checkArgument(minParticipantCount <= maxParticipantCount
			&& maxParticipantCount >= participantCount);

		this.minParticipantCount = minParticipantCount;
		this.maxParticipantCount = maxParticipantCount;
		updateStatus();
	}

}
