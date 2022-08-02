package com.prgrms.rg.web.user.results;

import java.time.LocalDate;

import com.prgrms.rg.domain.user.model.information.MannerInfo;

import lombok.Data;

@Data
public final class MannerInfoResult {

	private String level;
	private short noShow;
	private LocalDate bannedUntil;

	private MannerInfoResult(String level, short noShow, LocalDate bannedUntil) {
		this.level = level;
		this.noShow = noShow;
		this.bannedUntil = bannedUntil;
	}

	public static MannerInfoResult of(MannerInfo mannerInfo) {
		return new MannerInfoResult(mannerInfo.getLevel().toString(), mannerInfo.getNoShow(),
			mannerInfo.getBannedUntil());
	}
}
