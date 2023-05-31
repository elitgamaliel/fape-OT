package com.inretailpharma.digital.ordertracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderTrackerDetailDto {
    Long getOrderNumber();
    String getPaymentMethod();
    BigDecimal getPaymentAmount();
    String getPaymentDescription();
    BigDecimal getTotalAmount();
    BigDecimal getChangeAmount();
    String getOrderStatus();
    String getMotorizedId();
    String getTravel();
    LocalDateTime getScheduledOrderDate();
}
