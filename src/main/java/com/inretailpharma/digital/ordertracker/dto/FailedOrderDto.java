package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailedOrderDto implements Serializable {

	private Long orderId;
	private String reason;
}
