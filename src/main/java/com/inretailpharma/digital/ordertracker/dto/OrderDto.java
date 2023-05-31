package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto implements Serializable {

    // Canonical IDs
    private Long id;
    private String orderTrackingCode;
    private Long ecommerceId;
    private Long trackerId;
    private Long externalId;
    private Long bridgePurchaseId;
    private Long deliveryManagerId;

    private String motorizedId;

    private BigDecimal deliveryCost;
    private BigDecimal discountApplied;
    private BigDecimal subTotalCost;
    private BigDecimal totalAmount;

    // Canonical local and company
    private String localCode;
    private String local;
    private String company;
    private String companyCode;
    private Long drugstoreId;

    // Canonical data order
    private String source;
    private String groupName;
    private Integer groupOrder;
    private String orderStatusDate;
    private String updatedBy;

    private String statusNote;
    private String customNote;

    // Canonical status
    private OrderStatusDto orderStatus;

    // Canonical client
    private ClientDto client;

    // Canonical Schedule
    private ScheduleDto scheduled;

    // Canonical receipt
    private ReceiptDto receipt;

    // Canonical Payment Method
    private PaymentMethodDto paymentMethod;

    // Canonical Address delivery
    private AddressDto address;

    // Canonical OrderDetail
    private OrderDetailDto orderDetail;

    // Canonical OrderItems
    private List<OrderItemDto> orderItems;
    
    // Canonical Shelf
    private List<ShelfDto> shelfList;
    private String payBackEnvelope;
    private UserDto userDto;
    private BigDecimal totalCost;
    private boolean externalRouting;
}
