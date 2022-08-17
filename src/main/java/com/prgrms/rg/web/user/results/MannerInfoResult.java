package com.prgrms.rg.web.user.results;

import java.time.LocalDate;

import com.prgrms.rg.domain.user.model.information.MannerInfo;

import lombok.Data;

@Data
public final class MannerInfoResult {

	private int mannerPoint;
	private short noShow;
	private LocalDate bannedUntil;

	private MannerInfoResult(int mannerPoint, short noShow, LocalDate bannedUntil) {
		this.mannerPoint = mannerPoint;
		this.noShow = noShow;
		this.bannedUntil = bannedUntil;
	}

	public static MannerInfoResult of(MannerInfo mannerInfo) {
		return new MannerInfoResult(mannerInfo.getMannerPoint(), mannerInfo.getNoShow(),
			mannerInfo.getBannedUntil());
	}
}
