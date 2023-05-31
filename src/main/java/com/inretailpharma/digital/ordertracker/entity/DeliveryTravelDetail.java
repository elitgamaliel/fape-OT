package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Embeddable
@NoArgsConstructor
@Table(name = "delivery_travel_detail")
public class DeliveryTravelDetail implements Serializable {

	@Column(name = "order_group")
	private Integer orderGroup;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderTracker orderTracker;
	@Column(name = "projected_eta")
	private Integer projectedEta;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "final_status")
    private OrderTrackerStatus.Code finalStatus;
    @Column(name = "wait_time")
    private Integer waitTime;
    @Column(name = "pos_code")
    private String posCode;


    public DeliveryTravelDetail(OrderTracker orderTracker) {
        this.orderTracker = orderTracker;
    }
}
