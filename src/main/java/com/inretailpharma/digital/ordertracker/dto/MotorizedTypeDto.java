package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.Data;

@Data
public class MotorizedTypeDto implements Serializable {	
	
	private String imei;
	private String type;
	private Double latitude;
	private Double longitude;
	private Constant.MotorizedType currentMotorizedType;
	
	public MotorizedTypeDto() {}
	
	public MotorizedTypeDto(String type) {
		this.type = type;
	}
}
