package com.prgrms.rg.domain.user.model.information;

import java.util.List;

import com.prgrms.rg.domain.ridingpost.application.information.RidingPostBriefInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ParticipatedRidingInfo {
	List<? extends RidingPostBriefInfo> leading;
	List<? extends RidingPostBriefInfo> finished;
	List<? extends RidingPostBriefInfo> scheduled;

	private ParticipatedRidingInfo(List<? extends RidingPostBriefInfo> leading,
		List<? extends RidingPostBriefInfo> finished,
		List<? extends RidingPostBriefInfo> scheduled) {
		this.leading = leading;
		this.finished = finished;
		this.scheduled = scheduled;
	}

	public static ParticipatedRidingInfo from(List<? extends RidingPostBriefInfo> leading,
		List<? extends RidingPostBriefInfo> finished,
		List<? extends RidingPostBriefInfo> scheduled) {
		return new ParticipatedRidingInfo(leading, finished, scheduled);
	}
}
