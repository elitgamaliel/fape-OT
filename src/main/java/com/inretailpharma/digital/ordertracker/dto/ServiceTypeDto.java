package com.inretailpharma.digital.ordertracker.dto;

import lombok.Data;

@Data
public class ServiceTypeDto {

    private String code;
    private String name;
    private String type;
    private Integer leadTime;
    private Integer daysToPickup;
    private String startHour;
    private String endHour;
}