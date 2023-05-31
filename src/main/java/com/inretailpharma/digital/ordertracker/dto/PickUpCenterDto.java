package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PickUpCenterDto implements Serializable {

	private Long id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String localCode;
}
