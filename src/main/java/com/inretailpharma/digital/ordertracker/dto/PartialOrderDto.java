package com.inretailpharma.digital.ordertracker.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PartialOrderDto implements Serializable {

    private Long ecommercePurchaseId;

    private BigDecimal totalCost;
    private BigDecimal deliveryCost;

    private List<OrderItemDto> orderItem;
    private PaymentMethodDto payment;
}
