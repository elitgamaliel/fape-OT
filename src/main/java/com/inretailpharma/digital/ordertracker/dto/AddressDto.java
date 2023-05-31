package com.inretailpharma.digital.ordertracker.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class AddressDto implements Serializable{

    private String name;
    private String department;
    private String province;
    private String district;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String postalCode;
    private String notes;
    private String apartment;
    private String number;
}
