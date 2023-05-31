package com.inretailpharma.digital.ordertracker.mapper;

import com.inretailpharma.digital.ordertracker.dto.RouteDto;
import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryTravelMapper {

    public static RouteDto mapDeliveryTraveltoRouteDto(DeliveryTravel deliveryTravel) {
        RouteDto routeDto = null;
        if (deliveryTravel != null) {
            routeDto = RouteDto.builder()
                    .id(deliveryTravel.getId())
                    .groupName(deliveryTravel.getGroupName())
                    .motorizedId(deliveryTravel.getMotorizedId())
                    .dateCreated(deliveryTravel.getDateCreated())
                    .travelStatus(deliveryTravel.getTravelStatus())
                    .projectedEta(deliveryTravel.getProjectedEta())
                    .startDate(deliveryTravel.getStartDate())
                    .endDate(deliveryTravel.getEndDate())
                    .pickUpCenterName(deliveryTravel.getPickUpCenterName())
                    .pickUpCenterLatitude(deliveryTravel.getPickUpCenterLatitude())
                    .pickUpCenterLongitude(deliveryTravel.getPickUpCenterLongitude())
                    .build();
        }
        return routeDto;
    }

    public static List<RouteDto> convertDeliveryTravelToRouteDto(List<DeliveryTravel> deliveryTravelList) {
        List<RouteDto> routeDtoList = new ArrayList<>();
        if (deliveryTravelList != null) {
            routeDtoList = deliveryTravelList.stream().map(DeliveryTravelMapper::mapDeliveryTraveltoRouteDto).collect(Collectors.toList());
        }
        return routeDtoList;
    }

}
