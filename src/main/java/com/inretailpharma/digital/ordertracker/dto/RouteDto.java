package com.inretailpharma.digital.ordertracker.dto;

import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class RouteDto {

    private Long id;
    private String groupName;
    private String motorizedId;
    private Date dateCreated;
    private DeliveryTravel.TravelStatus travelStatus;
    private Integer projectedEta;
    private Date startDate;
    private Date endDate;
    private String pickUpCenterName;
    private BigDecimal pickUpCenterLatitude;
    private BigDecimal pickUpCenterLongitude;

}
