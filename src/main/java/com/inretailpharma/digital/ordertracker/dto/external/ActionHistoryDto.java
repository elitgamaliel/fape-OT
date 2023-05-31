package com.inretailpharma.digital.ordertracker.dto.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionHistoryDto {
	
	private String action;
	private String actionDate;
	private String orderCancelCode;
	private String orderCancelObservation;
	private String motorizedId;
}
