package com.inretailpharma.digital.ordertracker.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto implements Serializable {

    private String productCode;
    private String productName;
    private String productSku;
    private String shortDescription;
    private String brand;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Boolean fractionated;

    private boolean removed;
    private boolean edited;
}
