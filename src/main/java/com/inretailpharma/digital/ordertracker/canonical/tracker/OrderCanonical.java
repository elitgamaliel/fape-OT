package com.inretailpharma.digital.ordertracker.canonical.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCanonical {
    private Long id;
    private Long ecommercePurchaseId;
    private BigDecimal totalAmount;
    private BigDecimal deliveryCost;
    private String local;
    private String localCode;
    private Long orderExternalId;
    private Boolean partial;

    private ClientCanonical client;

    private AddressCanonical address;

    private PaymentMethodCanonical paymentMethod;

    private ReceiptCanonical receipt;

    private OrderDetailCanonical orderDetail;
}
