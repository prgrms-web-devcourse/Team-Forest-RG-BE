package com.prgrms.rg.domain.user.model;

import static lombok.AccessLevel.*;

import java.time.LocalDate;

import javax.persistence.Embeddable;

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
		return new Manner((short)0, (short)0, null);
	}

	public static Manner of(short point, short noShow, LocalDate banned) {
		return new Manner(point, noShow, banned);
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

	@Override
	public String toString() {
		return "Manner{" +
			"\n\tpoint=" + point +
			"\n\tnoShow=" + noShow +
			"\n\tbannedUntil=" + bannedUntil +
			"\n}";
	}

	enum MannerLevel {
		//TODO: 명칭 통일
		NORMAL, FOUR_LEGS_BICYCLE, THREE_LEGS_BICYCLE, TWO_LEGS_BICYCLE, LEGENDARY_RIDER;

		private static final short[] REQUIRED_FOR_PROMOTION = new short[] {500, 1000, 2000, 4000};

		public static MannerLevel of(short point) {
			if (point >= REQUIRED_FOR_PROMOTION[3]) {
				return LEGENDARY_RIDER;
			}
			if (point >= REQUIRED_FOR_PROMOTION[2]) {
				return TWO_LEGS_BICYCLE;
			}
			if (point >= REQUIRED_FOR_PROMOTION[1]) {
				return THREE_LEGS_BICYCLE;
			}
			if (point >= REQUIRED_FOR_PROMOTION[0]) {
				return FOUR_LEGS_BICYCLE;
			}
			return NORMAL;
		}
	}
}
