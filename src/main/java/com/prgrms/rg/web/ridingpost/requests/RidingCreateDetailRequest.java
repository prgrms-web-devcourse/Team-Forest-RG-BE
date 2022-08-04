package com.prgrms.rg.web.ridingpost.requests;

import java.util.List;

import com.prgrms.rg.domain.ridingpost.application.command.RidingSubCreateCommand;

import lombok.Data;

@Data
public class RidingCreateDetailRequest {

	private String subTitle;

	private String content;

	//todo validation - list 길이 제한
	private List<Long> images;

	public RidingSubCreateCommand toCommand() {
		return new RidingSubCreateCommand(subTitle, content, images);
	}
}