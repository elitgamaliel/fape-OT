package com.inretailpharma.digital.ordertracker.dto.external;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ExternalMotorizedStatusDto implements Serializable {
	
	private String statusName;
	private Long statusDate;
	private Double latitude;
	private Double longitude;
}
