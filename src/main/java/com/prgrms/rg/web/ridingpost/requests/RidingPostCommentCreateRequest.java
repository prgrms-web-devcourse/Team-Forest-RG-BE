package com.prgrms.rg.web.ridingpost.requests;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RidingPostCommentCreateRequest {
	@NotNull
	@Length(min = 1, max = 500)
	private String content;
}
