package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrugstoreDto {
	private String id;
	private String name;
	private String description;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String localCode;
	private String address;
	private Boolean enabled;
	private Long legacyId;
	private String localType;
}
