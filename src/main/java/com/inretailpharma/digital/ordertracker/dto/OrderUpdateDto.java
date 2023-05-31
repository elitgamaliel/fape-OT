package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.Data;

@Data
public class OrderUpdateDto implements Serializable {

	private Long orderId;
	private String groupName;
	private String previousStatus;
	private Constant.MotorizedType currentType;
}
