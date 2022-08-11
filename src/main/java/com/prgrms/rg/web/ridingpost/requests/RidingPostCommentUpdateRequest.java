package com.prgrms.rg.web.ridingpost.requests;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RidingPostCommentUpdateRequest {
	@NotEmpty
	private String contents;

}
