package com.inretailpharma.digital.ordertracker.canonical.tracker;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentMethodCanonical {
    private String type;
    private String cardProvider;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
}
