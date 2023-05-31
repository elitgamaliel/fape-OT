package com.inretailpharma.digital.ordertracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DrugstoreHeaderDto {
	private String name;
	private String localCode;
	private String localType;
	private BigDecimal latitude;
	private BigDecimal longitude;

}
