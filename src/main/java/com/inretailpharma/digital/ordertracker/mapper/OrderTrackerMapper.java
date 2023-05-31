package com.inretailpharma.digital.ordertracker.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.entity.AddressTracker;
import com.inretailpharma.digital.ordertracker.entity.Client;
import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerDetail;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerItem;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.PaymentMethod;
import com.inretailpharma.digital.ordertracker.entity.ReceiptType;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.entity.custom.Scheduled;
import com.inretailpharma.digital.ordertracker.entity.custom.Shelf;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

public class OrderTrackerMapper {

    private OrderTrackerMapper() {
    }
    
    private static final String TIME_FORMAT = "HH:mm";

    public static OrderDto mapOrderTrackerToDto(OrderTracker orderTracker) {
        OrderDto orderDto = null;
        if (orderTracker != null) {
            orderDto = OrderDto.builder()
                    .id(orderTracker.getId())
                    .orderTrackingCode(orderTracker.getOrderTrackingCode())
                    .ecommerceId(orderTracker.getEcommercePurchaseId())
                    .externalId(orderTracker.getExternalPurchaseId())
                    .deliveryManagerId(orderTracker.getDeliveryManagerId())
                    .deliveryCost(orderTracker.getDeliveryCost())
                    .totalAmount(orderTracker.getTotalCost())
                    .orderStatus(mapOrderStatusToDto(orderTracker.getOrderTrackerStatus()))
                    .orderStatusDate(DateUtil.getDateTimeFormatted(orderTracker.getOrderStatusDate()))
                    .client(mapClientToDto(orderTracker.getClient()))
                    .address(mapAddressToDto(orderTracker.getAddressTracker()))
                    .scheduled(mapScheduledToDto(orderTracker.getScheduled()))
                    .orderItems(mapOrderItemListToDto(orderTracker.getOrderItemList()))
                    .orderDetail(mapOrderTrackerDetailToDto(orderTracker.getOrderTrackerDetail()))
                    .receipt(mapReceiptTypeToDto(orderTracker.getReceiptType()))
                    .paymentMethod(mapPaymentMethodToDto(orderTracker.getPaymentMethod()))
                    .company(getCompanyCode(orderTracker.getOrderTrackerDetail()))
                    .shelfList(mapShelfDto(orderTracker.getShelfList()))
                    .payBackEnvelope(orderTracker.getPayBackEnvelope())
                    .motorizedId(getMotorizedId(orderTracker.getMotorized()))
                    .localCode(orderTracker.getLocalCode())
                    .groupName(orderTracker.getGroupName())
                    .userDto(userToUserDto(orderTracker.getMotorized()))
                    .build();
        }
        return orderDto;
    }

    private static UserDto userToUserDto(User user){
        if(user!=null) {
            UserDto userDto = new UserDto();
            userDto.setFirstName(user.getFirstName());
            userDto.setDni(user.getDni());
            userDto.setLastName(user.getLastName());
            userDto.setPhoneNumber(user.getPhone());
            return userDto;
        }else{
            UserDto userDto = new UserDto();
            userDto.setFirstName("-");
            userDto.setDni("-");
            userDto.setLastName("-");
            userDto.setPhoneNumber("-");
            return userDto;
        }
    }

    public static OrderDto mapOrderTrackerToDtoForMotorized(OrderTracker orderTracker) {
        OrderDto orderDto = null;
        if (orderTracker != null) {
            orderDto = OrderDto.builder()
                    .id(orderTracker.getId())
                    .orderTrackingCode(orderTracker.getOrderTrackingCode())
                    .ecommerceId(orderTracker.getEcommercePurchaseId())
                    .externalId(orderTracker.getExternalPurchaseId())
                    .client(mapClientToDto(orderTracker.getClient()))
                    .address(mapAddressToDto(orderTracker.getAddressTracker()))
                    .scheduled(mapScheduledToDto(orderTracker.getScheduled()))
                    .orderDetail(mapOrderTrackerDetailToDto(orderTracker.getOrderTrackerDetail()))
                    .build();
        }
        return orderDto;
    }
    
    public static OrderCancelledDto mapOrderTrackerToOrderCancelledDto(OrderTracker orderTracker) {
    	OrderCancelledDto orderDto = null;
        if (orderTracker != null) {
            orderDto = OrderCancelledDto.builder()
                    .ecommerceId(orderTracker.getEcommercePurchaseId())
                    .client(mapClientToDto(orderTracker.getClient()))
                    .cancellationDate(DateUtil.getTimeFormatted(orderTracker.getOrderStatusDate().toLocalTime(), TIME_FORMAT))
                    .build();

            if (orderTracker.getScheduled() != null) {
            	orderDto.setStartDate(DateUtil.getTimeFormatted(orderTracker.getScheduled().getStartDate().toLocalTime(), TIME_FORMAT));
            	orderDto.setEndDate(DateUtil.getTimeFormatted(orderTracker.getScheduled().getEndDate().toLocalTime(), TIME_FORMAT));
            }
        }
        return orderDto;
    }


    public static OrderStatusDto mapOrderStatusToDto(OrderTrackerStatus orderStatus) {
        OrderStatusDto orderStatusDto = null;
        if (orderStatus != null) {
            orderStatusDto = OrderStatusDto.builder()
                    .code(orderStatus.getCode().name())
                    .detail(orderStatus.getDescription())
                    .build();
        }
        return orderStatusDto;
    }

    public static ClientDto mapClientToDto(Client client) {
        ClientDto clientDto = null;
        if (client != null) {
            clientDto = ClientDto.builder()
                    .fullName(client.getFirstName())
                    .lastName(client.getLastName())
                    .email(client.getEmail())
                    .documentNumber(client.getDni())
                    .phone(client.getPhone())
                    .birthDate(DateUtil.getDateFormatted(client.getBirthDate()))
                    .hasInkaClub(Constant.Logical.Y.name().equals(client.getInkaClubClient()) ? Constant.Integers.ONE : Constant.Integers.ZERO)
                    .anonimous(Constant.Logical.Y.equals(client.getIsAnonymous()) ? Constant.Integers.ONE : Constant.Integers.ZERO)
                    .build();
        }
        return clientDto;
    }

    public static AddressDto mapAddressToDto(AddressTracker address) {
        AddressDto addressDto = null;
        if (address != null) {
            addressDto = AddressDto.builder()
                    .name(address.getStreet())
                    .department(address.getDepartment())
                    .province(address.getProvince())
                    .district(address.getDistrict())
                    .country(address.getCountry())
                    .latitude(address.getLatitude())
                    .longitude(address.getLongitude())
                    .notes(address.getNotes())
                    .apartment(address.getApartment())
                    .number(address.getNumber())
                    .build();
        }
        return addressDto;
    }

    public static ScheduleDto mapScheduledToDto(Scheduled scheduled) {
        ScheduleDto scheduledDto = null;
        if (scheduled != null) {
            scheduledDto = ScheduleDto.builder()
                    .startDate(scheduled.getStartDate())
                    .endDate(scheduled.getEndDate())
                    .build();
        }
        return scheduledDto;
    }

    public static OrderItemDto mapOrderItemToDto(OrderTrackerItem item) {
        OrderItemDto orderItemDto = null;
        if (item != null) {
            orderItemDto = OrderItemDto.builder()
                    .productCode(item.getProductCode())
                    .productName(item.getName())
                    .productSku(item.getProductSku())
                    .shortDescription(item.getShortDescription())
                    .brand(item.getBrand())
                    .fractionated(item.getFractionated().value())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                    .build();
        }
        return orderItemDto;
    }

    public static List<OrderItemDto> mapOrderItemListToDto(List<OrderTrackerItem> itemList) {
        List<OrderItemDto> orderItemDtoList = null;
        if (itemList != null) {
            orderItemDtoList = itemList.stream().map(OrderTrackerMapper::mapOrderItemToDto).collect(Collectors.toList());
        }
        return orderItemDtoList;
    }

    public static OrderDetailDto mapOrderTrackerDetailToDto(OrderTrackerDetail orderDetail) {
        OrderDetailDto orderDetailDto = null;
        if (orderDetail != null) {
            orderDetailDto = OrderDetailDto.builder()
                    .serviceCode(orderDetail.getServiceTypeCode())
                    .serviceName(orderDetail.getServiceTypeDescription())
                    .attempt(orderDetail.getAttempt())
                    .leadTime(orderDetail.getDeliveryLeadTime())
                    .startHour(DateUtil.getTimeFormatted(orderDetail.getStartHourPickup()))
                    .endHour(DateUtil.getTimeFormatted(orderDetail.getEndHourPickup()))
                    .programmed(Constant.Integers.ONE.equals(orderDetail.getReprogrammed()))
                    .build();
        }
        return orderDetailDto;
    }

    public static ReceiptDto mapReceiptTypeToDto(ReceiptType receiptType) {
        ReceiptDto receiptDto = null;
        if (receiptType != null) {
            receiptDto = ReceiptDto.builder()
                    .note(receiptType.getName())
                    .ruc(receiptType.getRuc())
                    .companyName(receiptType.getCompanyName())
                    .address(receiptType.getCompanyAddress())
                    .build();
        }
        return receiptDto;
    }

    public static PaymentMethodDto mapPaymentMethodToDto(PaymentMethod paymentMethod) {
        PaymentMethodDto paymentMethodDto = null;
        if (paymentMethod != null) {
            paymentMethodDto = PaymentMethodDto.builder()
                    .type(paymentMethod.getPaymentType().name())
                    .description(paymentMethod.getDescription())
                    .cardProvider(paymentMethod.getCardProvider())
                    .paidAmount(paymentMethod.getPaidAmount())
                    .changeAmount(paymentMethod.getChangeAmount())
                    .note(paymentMethod.getPaymentNote())
                    .build();
        }
        return paymentMethodDto;
    }

    public static List<ShelfDto> mapShelfDto(List<Shelf> shelfList) {
        List<ShelfDto> shelfDtoList = null;
        if (shelfList != null) {
            shelfDtoList = shelfList.stream()
                    .map(shelf -> ShelfDto.builder().lockCode(shelf.getLockCode()).packCode(shelf.getPackCode()).build())
                    .collect(Collectors.toList());
        }
        return shelfDtoList;
    }

    public static String getCompanyCode(OrderTrackerDetail orderDetail) {
        String companyCode = null;
        if (orderDetail != null) {
            companyCode = orderDetail.getCompanyCode();
        }
        return companyCode;
    }

    public static String getMotorizedId(User user) {
        String motorizedId = null;
        if (user != null) {
            motorizedId = user.getId();
        }
        return motorizedId;
    }
}
