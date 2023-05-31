package com.inretailpharma.digital.ordertracker.dto;

import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderHeaderDto implements Serializable {

    private Long inkaDeliveryId;
    private Long orderExternalId;
    private String clientName;
    private String clientLastName;
    private String statusName;
    private String statusDate;

    public OrderHeaderDto(OrderDto order) {
        this.inkaDeliveryId = order.getDeliveryManagerId();
        this.orderExternalId = order.getExternalId();
        this.clientName = order.getClient().getFullName();
        this.clientLastName = order.getClient().getLastName();
        this.statusName = order.getOrderStatus().getCode();
        this.statusDate = order.getOrderStatusDate();
    }

    public OrderHeaderDto(OrderTracker order) {
        this.inkaDeliveryId = order.getDeliveryManagerId();
        this.orderExternalId = order.getExternalPurchaseId();
        this.clientName = order.getClient().getFirstName();
        this.clientLastName = order.getClient().getLastName();
        this.statusName = order.getOrderTrackerStatus().getCode().name();
        this.statusDate = DateUtil.getDateTimeFormatted(order.getOrderStatusDate());
    }
}
