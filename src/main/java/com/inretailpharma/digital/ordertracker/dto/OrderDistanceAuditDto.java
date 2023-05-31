package com.inretailpharma.digital.ordertracker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDistanceAuditDto {

    private String ecommerceId;
    private String trackingCode;
    private String user;
    private BigDecimal userLatitude;
    private BigDecimal userLongitude;
    private boolean userGps;
    private Float distance;
    private BigDecimal deliveryLatitude;
    private BigDecimal deliveryLongitude;

}
