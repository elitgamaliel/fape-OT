package com.inretailpharma.digital.ordertracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFulfillmentDto {
    
    private String payOrderDate;
    private String transactionOrderDate;
    private String purchaseNumber;
    private String scheduledOrderDate;
    private Long paymentMethodId;
    private Long creditCardId;
    private String confirmedOrder;
    private String currency;
    private String orderStatus;
}
