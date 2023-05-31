package com.inretailpharma.digital.ordertracker.dto.external;

import lombok.Data;

@Data
public class ConfirmAttendanceRespDto {	
	
	private String result;
	private String resultTitle;
	private String resultDetail;
	
	public ConfirmAttendanceRespDto() {}
	
	public ConfirmAttendanceRespDto(String response) {
		this.result = response;
	}
	
	public ConfirmAttendanceRespDto(String response, String resultTitle, String resultDetail) {
		this.result = response;
		this.resultTitle = resultTitle;
		this.resultDetail = resultDetail;
	}
}
