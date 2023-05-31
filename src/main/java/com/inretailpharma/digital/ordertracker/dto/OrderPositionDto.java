package com.inretailpharma.digital.ordertracker.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderPositionDto {
	private String trackingCode;
    private Integer position;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double distance;
    private Integer priority;
    private Long deliveryTime;
}
