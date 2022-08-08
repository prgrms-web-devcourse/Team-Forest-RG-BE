package com.prgrms.rg.web.ridingpost.requests;

import java.util.List;

import javax.validation.constraints.Size;

import com.prgrms.rg.domain.ridingpost.application.command.RidingSubCreateCommand;

import lombok.Data;

@Data
public class RidingCreateDetailRequest {

	private String title;

	private String content;

	@Size(min = 0, max = 2)
	private List<Long> images;

	public RidingSubCreateCommand toCommand() {
		return new RidingSubCreateCommand(title, content, images);
	}
}
