package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCancelledDto implements Serializable {
	
	private Long ecommerceId;
	private String cancellationDate;
	private String startDate;
	private String endDate;
	private ClientDto client;
}
