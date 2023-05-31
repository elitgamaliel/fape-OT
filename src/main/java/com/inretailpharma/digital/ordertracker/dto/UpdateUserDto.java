package com.inretailpharma.digital.ordertracker.dto;

import com.inretailpharma.digital.ordertracker.utils.Constant.MotorizedType;

import lombok.Data;

@Data
public class UpdateUserDto {
	
	private String userId;
	private String action;
	private String statusName;
	private String code;
	private String localCode;
	private MotorizedType localType;
	private String imei;
}
