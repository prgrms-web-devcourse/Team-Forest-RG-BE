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
	List<? extends RidingPostBriefInfo> evaluabled;

	private ParticipatedRidingInfo(List<? extends RidingPostBriefInfo> leading,
		List<? extends RidingPostBriefInfo> finished,
		List<? extends RidingPostBriefInfo> scheduled,
		List<? extends RidingPostBriefInfo> evaluabled
	) {
		this.leading = leading;
		this.finished = finished;
		this.scheduled = scheduled;
		this.evaluabled = evaluabled;
	}

	public static ParticipatedRidingInfo from(List<? extends RidingPostBriefInfo> leading,
		List<? extends RidingPostBriefInfo> finished,
		List<? extends RidingPostBriefInfo> scheduled,
		List<? extends RidingPostBriefInfo> evaluabled
	) {
		return new ParticipatedRidingInfo(leading, finished, scheduled, evaluabled);
	}
}
