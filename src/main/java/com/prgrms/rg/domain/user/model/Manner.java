package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import javax.persistence.Embeddable;

import com.prgrms.rg.domain.user.model.information.MannerInfo;

import lombok.NoArgsConstructor;

//TODO: 비즈니스 로직이 명확해진 이후 테스트 추가
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Manner {

	private static final short NO_SHOW_LIMIT = 2;
	private static final short NO_SHOW_BAN_DURATION = 30;

	private short point;
	private short noShow;
	private LocalDate bannedUntil;

	private Manner(short point, short noShow, LocalDate bannedUntil) {
		this.point = point;
		this.noShow = noShow;
		this.bannedUntil = bannedUntil;
	}

	public static Manner create() {
		return new Manner((short)100, (short)0, null);
	}

	public static Manner of(short point, short noShow, LocalDate banned) {
		return new Manner(point, noShow, banned);
	}

	MannerInfo information() {
		return new MannerInfo(point, noShow, bannedUntil);
	}

	MannerLevel mannerLevel() {
		return MannerLevel.of(point);
	}

	void addNoShowCount() {
		noShow++;
		if (noShow % NO_SHOW_LIMIT == 0) {
			bannedUntil = ((bannedUntil != null) ? bannedUntil : LocalDate.now()).plusDays(NO_SHOW_BAN_DURATION);
		}
	}

	public void addPoint() {
		point += 10;
	}

	@Override
	public String toString() {
		return "Manner{" +
			"\n\tpoint=" + point +
			"\n\tnoShow=" + noShow +
			"\n\tbanned=" + bannedUntil +
			"\n}";
	}
}
