package com.inretailpharma.digital.ordertracker.dto.external;

import java.util.List;

import lombok.Data;

@Data
public class ExternalSyncDto {
	
	private Long orderId;
	private String motorizedId;
	private List<ExternalOrderStatusDto> statusOrderHistory;
}
