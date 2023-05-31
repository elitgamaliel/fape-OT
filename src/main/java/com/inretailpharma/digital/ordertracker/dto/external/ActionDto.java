package com.inretailpharma.digital.ordertracker.dto.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionDto {
	
	private long ecommerceId;
	private String action;
	private String orderCancelCode;
	private String orderCancelObservation;
	private String motorizedId;
	private String origin;	
    private String updatedBy;
}
