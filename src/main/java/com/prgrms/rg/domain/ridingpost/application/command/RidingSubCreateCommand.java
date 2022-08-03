package com.prgrms.rg.domain.ridingpost.application.command;

import java.util.List;

import lombok.Data;

@Data
public class RidingSubCreateCommand {

	private final String title;
	private final String content;
	private final List<Long> imageIdList;

}
