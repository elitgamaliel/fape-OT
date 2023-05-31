package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.time.LocalTime;

@Data
@Embeddable
@Table(name = "order_tracker_detail")
public class OrderTrackerDetail {

    @Column(table = "order_tracker_detail", name="delivery")
    private Integer delivery;

    @Column(table = "order_tracker_detail", name="service_type_code")
    private String serviceTypeCode;

    @Column(table = "order_tracker_detail", name="service_type_description")
    private String serviceTypeDescription;

    @Column(table = "order_tracker_detail", name="company_code")
    private String companyCode;

    @Column(table = "order_tracker_detail", name="company_description")
    private String companyDescription;

    @Column(table = "order_tracker_detail", name="center_code")
    private String centerCode;

    @Column(table = "order_tracker_detail", name="center_description")
    private String centerDescription;

    @Column(table = "order_tracker_detail", name="start_hour_zone")
    private LocalTime startHourZone;

    @Column(table = "order_tracker_detail", name="end_hour_zone")
    private LocalTime endHourZone;

    @Column(table = "order_tracker_detail", name="delivery_lead_time")
    private Integer deliveryLeadTime;

    @Column(table = "order_tracker_detail", name="days_to_pickup")
    private Integer daysToPickup;

    @Column(table = "order_tracker_detail", name="start_hour_pickup")
    private LocalTime startHourPickup;

    @Column(table = "order_tracker_detail", name="end_hour_pickup")
    private LocalTime endHourPickup;

    @Column(table = "order_tracker_detail", name="attempt")
    private Integer attempt;

    @Column(table = "order_tracker_detail", name="reprogrammed")
    private Integer reprogrammed;
}
