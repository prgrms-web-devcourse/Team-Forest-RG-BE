package com.prgrms.rg.domain.user.application.command;

import java.util.Arrays;
import java.util.Objects;

import com.prgrms.rg.domain.user.model.User;

import lombok.Getter;

@Getter
public class UserUpdateCommand {
	private Long id;
	private String nickname;
	private int ridingYears;
	private String ridingLevel;
	private String[] bicycles;
	private String introduction;

	public UserUpdateCommand(Long id, String nickname, int ridingYears, String ridingLevel, String[] bicycles,
		String introduction) {
		this.id = id;
		this.nickname = nickname;
		this.ridingYears = ridingYears;
		this.ridingLevel = ridingLevel;
		this.bicycles = bicycles;
		this.introduction = introduction;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UserUpdateCommand))
			return false;

		UserUpdateCommand that = (UserUpdateCommand)o;

		if (ridingYears != that.ridingYears)
			return false;
		if (!Objects.equals(id, that.id))
			return false;
		if (!Objects.equals(nickname, that.nickname))
			return false;
		if (!Objects.equals(ridingLevel, that.ridingLevel))
			return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(bicycles, that.bicycles))
			return false;
		return Objects.equals(introduction, that.introduction);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
		result = 31 * result + ridingYears;
		result = 31 * result + (ridingLevel != null ? ridingLevel.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(bicycles);
		result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
		return result;
	}
}
