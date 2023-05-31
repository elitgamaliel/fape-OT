package com.inretailpharma.digital.ordertracker.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class PaymentMethodDto implements Serializable {

    private String type;
    private String description;
    private String cardProvider;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private String note;
}
