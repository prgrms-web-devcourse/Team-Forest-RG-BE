package com.prgrms.rg.domain.user.model.information;

import java.util.List;

import com.prgrms.rg.domain.ridingpost.application.information.RidingPostBriefInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ParticipatedRidingInfo {
	List<RidingPostBriefInfo> leading;
	List<RidingPostBriefInfo> finished;
	List<RidingPostBriefInfo> scheduled;
	List<RidingPostBriefInfo> canEvaluated;

	private ParticipatedRidingInfo(List<RidingPostBriefInfo> leading, List<RidingPostBriefInfo> finished,
		List<RidingPostBriefInfo> scheduled, List<RidingPostBriefInfo> canEvaluated) {
		this.leading = leading;
		this.finished = finished;
		this.scheduled = scheduled;
		this.canEvaluated = canEvaluated;
	}

	public static ParticipatedRidingInfo from(List<RidingPostBriefInfo> leading, List<RidingPostBriefInfo> finished,
		List<RidingPostBriefInfo> scheduled, List<RidingPostBriefInfo> canEvaluated) {
		return new ParticipatedRidingInfo(leading, finished, scheduled, canEvaluated);
	}
}
