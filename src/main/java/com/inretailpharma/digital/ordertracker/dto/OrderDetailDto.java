package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailDto implements Serializable {

    // type of services
    private String serviceCode;
    private String serviceName;
    private String serviceType;
    // schedules
    private String confirmedSchedule;
    private String createdOrder;
    // attempts
    private Integer attempt;
    private Integer attemptTracker;

    private Integer leadTime;
    private String startHour;
    private String endHour;

    private boolean programmed;


}
