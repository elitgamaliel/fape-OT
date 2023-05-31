package com.inretailpharma.digital.ordertracker.entity;

import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Embeddable
@Table(name="order_tracker_item")
public class OrderTrackerItem {

    @Column(name = "product_code")
    private String productCode;
    @Column(name = "product_sku")
    private String productSku;
    @Column(name = "product_sap_code")
    private String productSapCode;
    @Column(name = "name")
    private String name;
    @Column(name = "short_description")
    private String shortDescription;
    private String brand;
    private Integer quantity;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    private Constant.Logical fractionated;

}
