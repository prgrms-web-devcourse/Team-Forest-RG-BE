package com.prgrms.rg.web.ridingpost.requests;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prgrms.rg.domain.ridingpost.model.Coordinate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RidingSaveMainRequest {

	@NotBlank
	private String title;

	private LocalDateTime ridingDate;

	//string
	@Pattern(regexp = "^[1-9]시간( 30분)?$|^((30)|(60))분|6시간 이상$")
	private String estimatedTime;

	//todo routes 길이 설정
	@Size(max = 10)
	private List<String> routes;
	private int fee;

	@Range(min = 5, max = 30)
	private int minParticipantCount;

	@Range(min = 5, max = 30)
	private int maxParticipantCount;
	private int regionCode;
	private Coordinate departurePlace;
	private String level;
	private List<String> bicycleTypes;
	private Long thumbnail;

}
