package com.prgrms.rg.domain.user.model.information;

import java.time.LocalDate;

import com.prgrms.rg.domain.user.model.MannerLevel;

import lombok.Getter;

@Getter
public final class MannerInfo {

	private int mannerPoint;
	private short noShow;
	private LocalDate bannedUntil;

	public MannerInfo(int mannerPoint, short noShow, LocalDate bannedUntil) {
		this.mannerPoint = mannerPoint;
		this.noShow = noShow;
		this.bannedUntil = bannedUntil;
	}

	@Override
	public String toString() {
		return "MannerInfo{" +
			"mannerPoint=" + mannerPoint +
			", noShow=" + noShow +
			", bannedUntil=" + bannedUntil +
			'}';
	}
}
