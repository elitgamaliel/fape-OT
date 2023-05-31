package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssignedOrdersResponseDto implements Serializable  {
	
	private List<Long> createdOrders;
	private List<FailedOrderDto> failedOrders;
	private String assigmentSuccessful;
	private String message;
}
