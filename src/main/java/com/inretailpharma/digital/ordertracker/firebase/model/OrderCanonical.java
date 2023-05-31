package com.inretailpharma.digital.ordertracker.firebase.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCanonical implements FirebaseObject {

    private String orderTrackingCode;
    private String motorizedId;
    private OrderStatusCanonical orderStatus;
    private String statusName;
    private TimesCanonical times;
    private GroupCanonical group;
    private int sort;
    private AddressLocation addressLocation;
    private Long orderExternalId;
    private String localType;
    /*-----*/
    @JsonIgnore
    private Long id;
    private Long ecommercePurchaseId;
    private BigDecimal totalAmount;
    private BigDecimal deliveryCost;
    private String local;
    private String localCode;


}
