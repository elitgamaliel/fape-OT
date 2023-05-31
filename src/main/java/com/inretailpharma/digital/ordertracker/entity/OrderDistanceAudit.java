package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "order_distance_audit")
public class OrderDistanceAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user;
    @Column(name = "user_latitude")
    private BigDecimal userLatitude;
    @Column(name = "user_longitude")
    private BigDecimal userLongitude;
    @Column(name = "user_gps")
    private boolean userGps;
    private String distance;
    @Column(name = "tracking_code")
    private String trackingCode;
    @Column(name = "ecommerceId")
    private Long ecommerceId;
    @Column(name = "delivery_latitude")
    private BigDecimal deliveryLatitude;
    @Column(name = "delivery_longitude")
    private BigDecimal deliveryLongitude;
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
