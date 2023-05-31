package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SyncOrderDto implements Serializable {

	private String trackingCode;
	private Long ecommerceId;
	private List<OrderStatusDto> statusOrderHistory;
}
