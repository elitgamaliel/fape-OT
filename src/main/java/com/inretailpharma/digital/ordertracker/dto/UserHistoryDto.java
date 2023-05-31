package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserHistoryDto implements Serializable {
	
	private String userId;
	private String statusCode;
	private String startDate;
	private String endDate;
	private String localName;	
}
