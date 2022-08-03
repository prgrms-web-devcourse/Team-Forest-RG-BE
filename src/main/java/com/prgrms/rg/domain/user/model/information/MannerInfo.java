package com.prgrms.rg.domain.user.model.information;

import java.time.LocalDate;

import com.prgrms.rg.domain.user.model.MannerLevel;

import lombok.Getter;

@Getter
public final class MannerInfo {

	private MannerLevel level;
	private short noShow;
	private LocalDate bannedUntil;

	public MannerInfo(MannerLevel level, short noShow, LocalDate bannedUntil) {
		this.level = level;
		this.noShow = noShow;
		this.bannedUntil = bannedUntil;
	}

	@Override
	public String toString() {
		return "MannerInfo{" +
			"level=" + level +
			", noShow=" + noShow +
			", bannedUntil=" + bannedUntil +
			'}';
	}
}
