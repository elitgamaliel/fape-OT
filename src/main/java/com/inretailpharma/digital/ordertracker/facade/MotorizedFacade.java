package com.inretailpharma.digital.ordertracker.facade;

import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.dto.resume.OrderTimeDto;
import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.service.order.OrdertrackerService;
import com.inretailpharma.digital.ordertracker.service.parameter.ParameterServiceImpl;
import com.inretailpharma.digital.ordertracker.service.system.DeliveryTravelService;
import com.inretailpharma.digital.ordertracker.service.user.UserService;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;
import com.inretailpharma.digital.ordertracker.utils.TimeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MotorizedFacade {

    @Autowired
    private DeliveryTravelService deliveryTravelService;

    @Autowired
    private OrdertrackerService ordertrackerService;

    @Autowired
    @Qualifier("trackerUserService")
    private UserService userService;


    @Autowired
    private ParameterServiceImpl parameterService;

    @Autowired
    private TimeFactory timeFactory;

    public List<OrderHeaderDto> getCancelledOrdersOfLastTravel(String motorizedId) {
        DeliveryTravel deliveryTravel = deliveryTravelService.findLastDeliveryTravel(motorizedId);
        List<OrderHeaderDto> ordersCancelled = new ArrayList<>();
        if (deliveryTravel != null) {
            deliveryTravel.getDetailList().forEach(deliveryTravelDetail -> {
                if (OrderTrackerStatus.Code.DISCARDED.contains(deliveryTravelDetail.getOrderTracker().getOrderTrackerStatus().getCode())) {
                    OrderDto order = ordertrackerService.findOrderByEcommercePurchaseId(deliveryTravelDetail.getOrderTracker().getEcommercePurchaseId());
                    ordersCancelled.add(new OrderHeaderDto(order));
                }
            });
        }
        return ordersCancelled;
    }

    public ReturnResumeDto getReturnResume(String motorizedId) {
        ReturnResumeDto returnResumeDto;
        DeliveryTravel deliveryTravel = deliveryTravelService.findLastDeliveryTravel(motorizedId);

        Integer delayTime = parameterService.parameterByCode(Constant.CUSTOMER_DELAY_TIME).getIntValue();

        List<OrderTimeDto> orderTimeList = deliveryTravel.getDetailList().stream().map(
                deliveryTravelDetail -> {
                    OrderTimeDto orderTimeDto = new OrderTimeDto();
                    orderTimeDto.fillWith(deliveryTravelDetail, delayTime);
                    return orderTimeDto;
                }).collect(Collectors.toList());

        Long timeSpan = DateUtil.timeBetween(timeFactory.currentDate(), deliveryTravel.getStartDate(), DateUtil.TimeUnits.MINUTES);

        returnResumeDto = new ReturnResumeDto(orderTimeList, timeSpan);
        return returnResumeDto;
    }

    public List<OrderDto> getAssignedOrders(String motorizedId) {
        List<OrderTrackerStatus> status =
                OrderTrackerStatus.Code.DISPATCHED.getChildren().stream()
                        .map(code -> OrderTrackerStatus.builder().code(code).build())
                        .collect(Collectors.toList());
        return ordertrackerService.findOrderByMotorized(motorizedId, status);
    }

    public List<OrderDto> getOrdersCurrentTravel(String motorizedId) {
        return Optional.ofNullable(deliveryTravelService.getGroupNameOfCurrentTravel(motorizedId))
                .map(groupName -> ordertrackerService.getCurrentOrdersByGroupName(groupName))
                .orElse(List.of());
    }

    public List<OrderCancelledDto> getCancelledOrdersHistorical(String motorizedId) {
        List<OrderTrackerStatus> status =
                OrderTrackerStatus.Code.DISCARDED.getChildren().stream()
                        .map(code -> OrderTrackerStatus.builder().code(code).build())
                        .collect(Collectors.toList());
        return ordertrackerService.findOrdersHistorical(motorizedId, status, LocalDate.now());
    }

    public MotorizedDetailDto getMotorizedInfoByEcommerceId(Long ecommerceId) {
        OrderDto order = ordertrackerService.findOrderByEcommercePurchaseId(ecommerceId);
        if (order != null && order.getMotorizedId() != null) {
            User user = userService.findUser(order.getMotorizedId());
            MotorizedDetailDto motorizedDetailDto = new MotorizedDetailDto();
            motorizedDetailDto.setDocument(user.getDni());
            String fullname = Optional.ofNullable(user.getFirstName()).orElse(StringUtils.EMPTY)
                    .toUpperCase()
                    .concat(" ")
                    .concat(Optional.ofNullable(user.getLastName()).orElse(StringUtils.EMPTY))
                    .toUpperCase();
            motorizedDetailDto.setName(fullname);
            motorizedDetailDto.setPhone(user.getPhone());
            motorizedDetailDto.setTravelGroup(order.getGroupName());
            return motorizedDetailDto;
        }
        return null;
    }

    public List<MotorizedDetailDto> listInfoMotorized(List<Long> ecommerceId) {
        log.info("M:listInfoMotorized:1");
        List<OrderDto> listOrder = ordertrackerService.listOrderInfoMotorized(ecommerceId);
        if (listOrder != null ) {
            List<MotorizedDetailDto> infoMotorized = listOrder.stream().parallel().map(item -> {
                MotorizedDetailDto motorizedDetailDto = new MotorizedDetailDto();
                if (item.getMotorizedId() != null) {
                    motorizedDetailDto.setDocument(item.getUserDto().getDni());
                    String fullname = Optional.ofNullable(item.getUserDto().getFirstName()).orElse(StringUtils.EMPTY)
                            .toUpperCase()
                            .concat(" ")
                            .concat(Optional.ofNullable(item.getUserDto().getLastName()).orElse(StringUtils.EMPTY))
                            .toUpperCase();
                    motorizedDetailDto.setName(fullname);
                    motorizedDetailDto.setPhone(item.getUserDto().getPhoneNumber());
                    motorizedDetailDto.setTravelGroup(item.getGroupName());
                    motorizedDetailDto.setEcommerceId(item.getEcommerceId());
                }
                return motorizedDetailDto;
            }).collect(Collectors.toList());
            return infoMotorized;
        }
        return null;
    }

    public List<RouteDto> getAssignedRoutes(String motorizedId) {
        return deliveryTravelService.getAssignedRoutes(motorizedId);
    }
}
